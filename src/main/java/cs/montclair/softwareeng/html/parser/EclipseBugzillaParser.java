package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;

public class EclipseBugzillaParser extends HtmlBugParser {

   @Override
   protected String parsePriority(Document doc) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   protected String parseIssueType(Document doc) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

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
