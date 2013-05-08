package cs.montclair.softwareeng.html.parser;

import cs.montclair.softwareeng.db.BugDAO;
import cs.montclair.softwareeng.model.Bug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   private static void printUsage() {
      System.out.println("Usage: java cs.montclair.softwareeng.html.parser.Main [chrome | netbeans] [dir]");
   }

   public static void main(String[] args) {
      if(args.length != 2) {
         printUsage();
         System.exit(1);
      }

      File path = new File(args[1]);
      String type = args[0];
      IHtmlBugParser parser = getParser(type);

      if(!path.isDirectory()) {
         try {
            Bug bug = parser.parse(path);
            System.out.println("Bug: " + bug);
            System.out.println("Description: " + bug.getDescription());
            BugDAO dao = new BugDAO();
            dao.save(bug);
         }
         catch(IOException e) {
            e.printStackTrace();
         }
      }
   }

   private static IHtmlBugParser getParser(String type) {
      if(type.equals("chrome")) {
         return new ChromiumBugzillaParser();
      }
      else if(type.equals("netbeans")) {
         return new NetbeansBugzillaParser();
      }

      throw new RuntimeException("Parser type not found: " + type);
   }

   private static List<Bug> parseAll(File dir, IHtmlBugParser parser) {
      List<Bug> bugs = new ArrayList<Bug>();

      for(File file : dir.listFiles()) {
         if(file.getName().equals("search.html")) {
            continue;
         }

         try {
            Bug bug = parser.parse(file);
            bugs.add(bug);

            //System.out.println("Bug parsed successfully: " + bug);
         }
         catch(IOException ioe) {
            ioe.printStackTrace();
         }
      }

      System.out.println("# bugs found: " + bugs.size());
      return bugs;
   }
}
