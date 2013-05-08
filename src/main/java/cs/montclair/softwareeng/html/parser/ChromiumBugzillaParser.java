package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.Bug;
import cs.montclair.softwareeng.model.BugType;
import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class ChromiumBugzillaParser extends HtmlBugParser {

   @Override
   public Bug parse(File file) throws IOException {
      Bug bug = super.parse(file);
      bug.setType(BugType.CHROME);

      return bug;
   }

   @Override
   protected String parseSummary(Document doc) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   protected String parsePriority(Document doc) {
      return null;
   }

   @Override
   protected String parseIssueType(Document doc) {
      return null;
   }

   @Override
   protected Timestamp parseCreatedDate(Document doc) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   protected String parseDescription(Document doc) {
      return doc.select(".vt.issuedescription pre").text();
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      Elements elems = doc.select("a[href*=chromium/source");

      if(elems.size() > 0) {
         String revision = elems.get(0).text();
         VCSCommit commit = new VCSCommit();
      }


      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      return doc.select("#meta-float td").get(0).text();
   }
}
