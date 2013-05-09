package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public abstract class HtmlBugParser implements IHtmlBugParser {

   private static final Logger LOG = LoggerFactory.getLogger(HtmlBugParser.class);

   public Bug parse(File file) throws Exception {
      LOG.debug("Parsing bug file: " + file);
      Document doc = Jsoup.parse(file, "utf-8");

      Bug bug = new Bug();

      bug.setId(Integer.valueOf(FilenameUtils.getBaseName(file.getName())));
      bug.setSummary(parseSummary(doc));
      bug.setDescription(parseDescription(doc));
      bug.setCommit(parseCommit(doc));
      bug.setIssueType(parseIssueType(doc));
      bug.setCreatedDate(parseCreatedDate(doc));

      String status = parseStatus(doc);
      String resolution = null;

      if(status.indexOf(" ") != -1) {
         String[] parts = status.split(" ");
         status = parts[0];
         resolution = parts[1];
      }

      bug.setStatus(BugStatus.valueOf(status));
      bug.setResolution(resolution == null ? null : BugResolution.valueOf(resolution));
      bug.setPriority(BugPriority.valueOf(parsePriority(doc)));

      return bug;
   }

   protected abstract String parseSummary(Document doc);

   protected abstract String parsePriority(Document doc);

   protected abstract String parseIssueType(Document doc);

   protected abstract Date parseCreatedDate(Document doc);

   protected abstract String parseDescription(Document doc);

   protected abstract VCSCommit parseCommit(Document doc);

   protected abstract String parseStatus(Document doc);
}