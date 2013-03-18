package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.Bug;

import java.io.File;
import java.io.IOException;

public interface IHtmlBugParser {

   public Bug parse(File file) throws IOException;
}
