package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.Bug;
import cs.montclair.softwareeng.model.VCSCommit;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public abstract class HtmlBugParser implements IHtmlBugParser {

   public Bug parse(File file) throws IOException {
      Document doc = Jsoup.parse(file, "utf-8");

      Bug bug = new Bug();
      bug.setDescription(parseDescription(doc));
      bug.setCommit(parseCommit(doc));

      return bug;
   }

   protected abstract String parseDescription(Document doc);

   protected abstract VCSCommit parseCommit(Document doc);

   protected abstract String parseStatus(Document doc);
}