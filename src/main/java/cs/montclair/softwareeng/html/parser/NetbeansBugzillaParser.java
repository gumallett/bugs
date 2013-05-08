package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.db.ChangedFileDao;
import cs.montclair.softwareeng.model.Bug;
import cs.montclair.softwareeng.model.BugType;
import cs.montclair.softwareeng.model.ChangedFile;
import cs.montclair.softwareeng.model.VCSCommit;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetbeansBugzillaParser extends HtmlBugParser {

   // Bugzilla date
   private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");

   // Mercurial date
   private final SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");

   // SHA-1 parser for revision hash
   private final Pattern hexPattern = Pattern.compile("\\s*([0-9]|[a-f]){12}\\s*$");

   // Patterns for parsing mercurial logs
   private final Pattern filesPattern = Pattern.compile("^files:", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
   private final Pattern datePattern = Pattern.compile("^date:", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
   private final Pattern userPattern = Pattern.compile("^user:", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

   public NetbeansBugzillaParser() {

   }

   @Override
   public Bug parse(File file) throws IOException {
      Bug bug = super.parse(file);
      bug.setType(BugType.NETBEANS);

      return bug;
   }

   @Override
   protected String parseSummary(Document doc) {
      return doc.select("#short_desc_nonedit_display").text();
   }

   @Override
   protected String parsePriority(Document doc) {
      Elements elems = doc.select("#bugzilla-body a[href=page.cgi?id=fields.html#priority]");
      Element elem = elems.get(0);

      elem = getTableRow(elem);
      elem = elem.child(1);
      String priority = elem.text();

      int idx = priority.indexOf("(vote)");

      if(idx != -1) {
         priority = priority.substring(0, idx).trim();
      }

      idx = priority.indexOf("with");

      if(idx != -1) {
         priority = priority.substring(0, idx).trim();
      }

      //System.out.println("td: "+elem+" priority: "+priority);
      return priority;
   }

   @Override
   protected String parseIssueType(Document doc) {
      return doc.select("#field_container_cf_bug_type").text();
   }

   @Override
   protected Date parseCreatedDate(Document doc) {
      Elements elems = doc.select("#bugzilla-body td.field_label");
      Element found = null;

      for(Element elem : elems) {
         if(elem.text().startsWith("Reported")) {
            found = getTableRow(elem);
            break;
         }
      }

      if(found == null) {
         return null;
      }

      Element dateTd = found.child(1);
      String dateText = dateTd.text();
      int idx = dateText.indexOf("by");

      if(idx > 0) {
         dateText = dateText.substring(0, idx).trim();
      }

      try {
         return sdf.parse(dateText);
      }
      catch(ParseException e) {
         e.printStackTrace();
      }

      return null;
   }

   @Override
   protected String parseDescription(Document doc) {
      return doc.select("#c0 pre.bz_comment_text").text();
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      Elements elems = doc.select("#bugzilla-body pre.bz_comment_text");

      for(Element elem : elems) {
         String text = elem.text();
         Matcher matcher = hexPattern.matcher(text);

         if(matcher.matches()) {
            VCSCommit commit = new VCSCommit();
            String revision = matcher.group();
            commit.setRevision(revision);

            Process hg = null;

            try {
               hg = getMercurialLog(revision);
               InputStream is = hg.getInputStream();

               Scanner scanner = new Scanner(is);

               scanner.findWithinHorizon(userPattern, 0);
               String user = scanner.nextLine().trim();
               scanner.findWithinHorizon(datePattern, 0);
               String date = scanner.nextLine().trim();
               scanner.findWithinHorizon(filesPattern, 0);
               List<ChangedFile> files = getChangedFiles(scanner);
               String message = consume(scanner);

               commit.setUser(user);
               commit.setFiles(files);
               commit.setCommitMessage(message);

               try {
                  commit.setCommitDate(sdf2.parse(date));
               }
               catch(ParseException e) {
                  e.printStackTrace();
               }

               //System.out.println(commit);

               hg.destroy();
            }
            catch(Exception e) {
               new Exception("Error getting revision log for rev #: "+revision).printStackTrace();
            }
            finally {
               if(hg != null) {
                  hg.destroy();
               }
            }

            return commit;
         }
      }

      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      return doc.select("#bz_field_status").text();
   }

   private static Element getTableRow(Element element) {
      if(element.tagName().equals("tr")) {
         return element;
      }

      return getTableRow(element.parent());
   }

   private static Process getMercurialLog(String revision) throws IOException {
      ProcessBuilder processBuilder = new ProcessBuilder("\"C:\\Program Files\\TortoiseHg\\hg.exe\"", "log", "-v", "-r", revision);
      processBuilder.redirectErrorStream(true);
      processBuilder.directory(new File("D:\\svn\\netbeans"));

      return processBuilder.start();
   }

   private static List<ChangedFile> getChangedFiles(Scanner in) {
      List<ChangedFile> files = new ArrayList<ChangedFile>();

      while(in.hasNext()) {
         String file = in.next();

         if(file.startsWith("description")) {
            break;
         }

         ChangedFile changedFile;
         ChangedFileDao dao = new ChangedFileDao();

         changedFile = dao.findByFileName(file);

         if(changedFile == null) {
            changedFile = new ChangedFile();
            changedFile.setFileName(file);
            changedFile.setExtension(FilenameUtils.getExtension(file));
            dao.save(changedFile);
         }

         files.add(changedFile);
      }

      return files;
   }

   private static String consume(Scanner in) {
      StringBuilder ret = new StringBuilder();

      while(in.hasNext()) {
         ret.append(in.nextLine().trim());
      }

      return ret.toString();
   }
}
