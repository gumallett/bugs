package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.Bug;

import java.io.File;
import java.io.IOException;

public class Main {

   private static void printUsage() {
      System.out.println("Usage: java cs.montclair.softwareeng.html.parser.Main [chrome | eclipse] [file]");
   }

   public static void main(String[] args) {
      if(args.length != 2) {
         printUsage();
         System.exit(1);
      }

      File file = new File(args[1]);
      String type = args[0];
      IHtmlBugParser parser = new ChromiumBugzillaParser();

      try {
         Bug bug = parser.parse(file);

         System.out.println("Bug parsed successfully: " + bug);
      }
      catch(IOException ioe) {
         ioe.printStackTrace();
      }
   }
}
