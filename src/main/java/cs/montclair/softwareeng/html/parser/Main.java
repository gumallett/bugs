package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.model.Bug;
import cs.montclair.softwareeng.model.Counter;
import cs.montclair.softwareeng.model.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   private static void printUsage() {
      System.out.println("Usage: java cs.montclair.softwareeng.html.parser.Main [chrome | eclipse] [dir]");
   }

   public static void main(String[] args) {
      if(args.length != 2) {
         printUsage();
         System.exit(1);
      }

      List<Bug> bugs = new ArrayList<Bug>();

      File dir = new File(args[1]);
      String type = args[0];
      IHtmlBugParser parser;

      if(type.equals("chrome")) {
         parser = new ChromiumBugzillaParser();
      }
      else {
         parser = new NetbeansBugzillaParser();
      }

      for(File file : dir.listFiles()) {
         if(file.getName().equals("search.html")) {
            continue;
         }

         try {
            Bug bug = parser.parse(file);
            bugs.add(bug);
            //System.out.println("status: "+bug.getStatus());

            //System.out.println("Bug parsed successfully: " + bug);
            //break;
         }
         catch(IOException ioe) {
            ioe.printStackTrace();
         }
      }

      System.out.println("# bugs found: " + bugs.size());

      Statistics stats = new Statistics(bugs);
      System.out.println(stats.getStatusCounts());
   }

}
