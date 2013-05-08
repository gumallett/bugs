package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.nodes.Document;

public class NetbeansBugzillaParser extends HtmlBugParser {

   public NetbeansBugzillaParser() {

   }

   @Override
   protected String parseDescription(Document doc) {
      return null;
   }

   @Override
   protected VCSCommit parseCommit(Document doc) {
      return null;
   }

   @Override
   protected String parseStatus(Document doc) {
      //System.out.println("doc: "+doc.toString());
      return doc.select("#bz_field_status").text();
   }
}
