package cs.montclair.softwareeng.html.parser;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Downloader {

   private static final String BUGZILLA_URL = "https://netbeans.org/bugzilla";
   private static final String DIR = "C:\\svn\\parser\\netbeans_bugs";

   private final File searchFile;
   private final String bugzillaUrl;
   private final String searchUrl;
   private final String outputDir;
   private final ExecutorService pool = Executors.newFixedThreadPool(8);

   public Downloader(String bugzillaUrl, String searchUri, String outputDir) {
      this.outputDir = outputDir;
      this.searchFile = new File(outputDir, "search.html");
      this.bugzillaUrl = bugzillaUrl;
      this.searchUrl = bugzillaUrl + "/" + searchUri;
   }

   public Connection.Response downloadSearchPage() throws IOException {
      System.out.println("Getting search file...");
      Connection.Response doc = getUrlWithJSoup(searchUrl);

      FileUtils.writeByteArrayToFile(searchFile, doc.bodyAsBytes());
      return doc;
   }

   public void downloadAllBugs() throws IOException {
      Document page = getSearchPage();
      Elements links = page.select(getBugLinkSelector());

      for(Element element : links) {
         pool.submit(new BugDownloader(element));
      }

      pool.shutdown();

      try {
         while(!pool.isTerminated()) {
            Thread.sleep(500);
         }
      }
      catch(InterruptedException e) {
         e.printStackTrace();
      }

      System.out.println("Num links: " + links.size());
   }

   /**
    * The CSS selector to use to parse the bug <a></a> link tags.
    */
   public String getBugLinkSelector() {
      return ".bz_buglist .bz_bugitem .bz_id_column a";
   }

   public Document getSearchPage() throws IOException {
      if(searchFile.exists()) {
         System.out.println("Parsing existing search file...");
         return Jsoup.parse(searchFile, "UTF-8");
      }
      else {
         Connection.Response resp = downloadSearchPage();
         System.out.println("Parsing search file...");
         return Jsoup.parse(searchFile, resp.charset());
      }
   }

   public static void main(String[] args) throws Exception {
      String search = "buglist.cgi?order=Importance;chfieldto=2011-04-01;query_format=advanced;chfield=[Bug%20creation];chfieldfrom=2006-01-01;bug_status=UNCONFIRMED;bug_status=NEW;bug_status=STARTED;bug_status=REOPENED;bug_status=RESOLVED;bug_status=VERIFIED;bug_status=CLOSED";
      Downloader downloader = new Downloader(BUGZILLA_URL, search, DIR);
      downloader.downloadAllBugs();
   }

   private static Connection.Response getUrlWithJSoup(String url) throws IOException {
      return Jsoup.connect(url).
              userAgent("Mozilla").
              data("name", "jsoup").
              timeout(600000).
              maxBodySize(0).execute();
   }

   private class BugDownloader implements Runnable {

      private final Element element;

      public BugDownloader(Element element) {
         this.element = element;
      }

      @Override
      public void run() {
         try {
            String linkUri = element.attr("href");
            String bugLink = bugzillaUrl + "/" + linkUri;
            String bugFilename = element.text() + ".html";

            System.out.println("Downloading bug: " + bugLink);

            Connection.Response bugPage = getUrlWithJSoup(bugLink);

            System.out.println("Writing bug file to: " + bugFilename);

            FileUtils.writeByteArrayToFile(new File(outputDir, bugFilename), bugPage.bodyAsBytes());
         }
         catch(Exception e) {
            e.printStackTrace();
         }
      }
   }
}
