package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChromiumBugzillaParser extends HtmlBugParser {

   @Override
   protected String parsePriority(Document doc) {
      return null;
   }

   @Override
   protected String parseIssueType(Document doc) {
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
         String revision = elems.get(0).text();
         return new VCSCommit(revision);
      }


      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      return doc.select("#meta-float td").get(0).text();
   }
}
