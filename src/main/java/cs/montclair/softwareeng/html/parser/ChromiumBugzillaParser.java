package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;

public class ChromiumBugzillaParser extends HtmlBugParser {

   @Override
   protected String parseDescription(Document doc) {
      return doc.select(".vt.issuedescription pre").text();
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      String revision = doc.select("a[href*=chromium/source").get(0).text();

      return new VCSCommit(revision);
   }

   @Override
   protected String parseStatus(Document doc) {
      return doc.select("#meta-float td").get(0).text();
   }
}
