package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.db.ChangedFileDao;
import cs.montclair.softwareeng.db.VCSCommitDao;
import cs.montclair.softwareeng.model.Bug;
import cs.montclair.softwareeng.model.BugType;
import cs.montclair.softwareeng.model.ChangedFile;
import cs.montclair.softwareeng.model.VCSCommit;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ChromiumBugzillaParser extends HtmlBugParser {

   private static final Logger LOG = LoggerFactory.getLogger(ChromiumBugzillaParser.class);

   private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   private static final SimpleDateFormat bugzillaSdf = new SimpleDateFormat("MMM dd, yyyy");

   @Override
   public Bug parse(File file) throws Exception {
      Bug bug = super.parse(file);
      bug.setType(BugType.CHROME);
      bug.setId(bug.getId() + 200000);

      return bug;
   }

   @Override
   protected String parseSummary(Document doc) {
      Elements elems = doc.select("#issueheader tr");
      Element firstRow = elems.get(0);

      Element secondCol = firstRow.child(1);
      return secondCol.text();
   }

   @Override
   protected String parsePriority(Document doc) {
      String priority = getMetaData(doc, "Pri-");

      if(priority == null) {
         priority = getMetaData(doc, "Size-");

         if(priority != null && priority.equalsIgnoreCase("high")) {
            priority = "1";
         }
         else if(priority != null && priority.equalsIgnoreCase("medium")) {
            priority = "2";
         }
         else {
            priority = "3";
         }
      }
      return "P" + priority;
   }

   @Override
   protected String parseIssueType(Document doc) {
      String type = getMetaData(doc, "Type-");
      return type == null ? null : type.toUpperCase();
   }

   @Override
   protected Date parseCreatedDate(Document doc) {
      String date = doc.select("td.issuedescription .date").text();

      try {
         return bugzillaSdf.parse(date);  //To change body of implemented methods use File | Settings | File Templates.
      }
      catch(ParseException e) {
         LOG.error(e.getMessage(), e);
      }

      return null;
   }

   @Override
   protected String parseDescription(Document doc) {
      return doc.select(".vt.issuedescription pre").text();
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      Elements elems = doc.select("a[href*=chromium/source");

      if(elems.size() > 0) {
         String revision = elems.last().text().substring(1);
         VCSCommitDao dao = new VCSCommitDao();

         VCSCommit commit = dao.findByRevision(revision);

         if(commit == null) {
            commit = getCommit(revision);
         }

         return commit;
      }


      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      String status = doc.select("#meta-float td").get(0).text();
      status = status.toUpperCase();

      if(status.equalsIgnoreCase("invalid")) {
         return "CLOSED INVALID";
      }
      else if(status.equalsIgnoreCase("wontfix")) {
         return "CLOSED WONTFIX";
      }
      else if(status.equalsIgnoreCase("duplicate")) {
         return "CLOSED DUPLICATE";
      }
      else if(status.equalsIgnoreCase("fixed")) {
         return "RESOLVED FIXED";
      }
      else if(status.equalsIgnoreCase("verified")) {
         return "VERIFIED FIXED";
      }

      String closed = getMetaData(doc, "Closed:");

      if(closed != null) {
         return "CLOSED " + status.toUpperCase();
      }

      return status;
   }

   private static VCSCommit getCommit(String revision) {
      VCSCommit commit = new VCSCommit();
      commit.setRevision(revision);
      Process svn;

      try {
         svn = getSVNLog(revision);
         InputStream is = svn.getInputStream();
         Scanner scanner = new Scanner(is);

         String line = scanner.nextLine();
         line = scanner.nextLine();
         String[] parts = line.split(" \\| ");
         String user = parts[1];
         String date = parts[2];

         int idx = date.indexOf("(");
         date = date.substring(0, idx - 1);

         String lines = parts[3];
         parts = lines.split(" ");
         int lineCount = Integer.valueOf(parts[0]);
         scanner.reset();
         scanner.nextLine();

         List<ChangedFile> changedFiles = new ArrayList<ChangedFile>();
         ChangedFileDao changedFileDao = new ChangedFileDao();

         while(scanner.hasNext()) {
            line = scanner.nextLine().trim();

            if(line.startsWith("M") || line.startsWith("A") || line.startsWith("D")) {
               String filename = line.substring(2);

               ChangedFile changedFile = changedFileDao.findByFileName(filename);

               if(changedFile == null) {
                  changedFile = new ChangedFile();
                  changedFile.setFileName(filename);
                  changedFile.setExtension(FilenameUtils.getExtension(filename));
                  changedFileDao.save(changedFile);
               }

               changedFiles.add(changedFile);
            }
            else {
               break;
            }
         }

         commit.setUser(user);

         try {
            commit.setCommitDate(sdf.parse(date));
         }
         catch(ParseException e) {
            LOG.error(e.getMessage(), e);
         }

         commit.setCommitMessage(getMessage(scanner, lineCount));
         commit.setFiles(changedFiles);
      }
      catch(Exception ioe) {
         LOG.warn("Could not get revision info for: {}", revision);
         LOG.warn(ioe.getMessage(), ioe);
      }

      return commit;
   }

   private static Process getSVNLog(String revision) throws IOException {
      ProcessBuilder processBuilder =
         new ProcessBuilder("\"C:\\Program Files\\CollabNet\\Subversion Client\\svn.exe\"", "log", "-v", "-r", revision);

      processBuilder.redirectErrorStream(true);
      processBuilder.directory(new File("Z:\\chrome"));

      return processBuilder.start();
   }

   private static String getMessage(Scanner in, int totalLines) {
      int linesRead = 0;
      StringBuilder builder = new StringBuilder();

      while(in.hasNext() && linesRead < totalLines) {
         builder.append(in.nextLine());
         linesRead++;
      }

      return builder.toString();
   }

   private static String getMetaData(Document doc, String startingWith) {
      Elements tableRows = doc.select("#issuemeta tr");

      for(Element elem : tableRows) {
         String text = elem.text();

         if(text.startsWith(startingWith)) {
            return text.substring(startingWith.length());
         }
      }

      return null;
   }
}
