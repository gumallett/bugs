package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;

public class EclipseBugzillaParser extends HtmlBugParser {

   @Override
   protected String parseDescription(Document doc) {
      return doc.select("#c0 .bz_comment_text").text();
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      return doc.select("#bz_field_status").text();
   }
}
