package cs.montclair.softwareeng.html.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class Downloader {

   private static final String DIR = "C:\\svn\\parser\\eclipse_bugs";
   private static final String SEARCH_URL = "https://bugs.eclipse.org/bugs/buglist.cgi?chfield=[Bug%20creation]&chfieldfrom=2000-01-01&chfieldto=2011-02-01&classification=Eclipse&component=IDE&product=Platform&query_format=advanced&order=changeddate%2Cbug_status%2Cpriority%2Cassigned_to%2Cbug_id&limit=0";

   public static void main(String[] args) throws Exception {
      Document page = getSearchPage();

      Elements links = page.select(".bz_id_column a");

      for(Element element : links) {
         String linkUrl = element.attr("href");
         Document bugPage = getUrlWithJSoup("https://bugs.eclipse.org/bugs/" + linkUrl);
         writeToFile(bugPage.toString(), new File(DIR, element.text() + ".html"));
      }

      System.out.println("Num links: " + links.size());
   }

   private static Document getSearchPage() throws IOException {
      File searchFile = new File(DIR, "\\search.html");

      if(searchFile.exists()) {
         return Jsoup.parse(searchFile, "UTF-8");
      }
      else {
         Document doc = getUrlWithJSoup(SEARCH_URL);

         writeToFile(doc.toString(), searchFile);
         return doc;
      }
   }

   private static void writeToFile(String string, File file) {
      FileOutputStream fos = null;

      try {
         fos = new FileOutputStream(file);
         PrintWriter writer = new PrintWriter(fos);
         writer.println(string);
         writer.flush();
      }
      catch(FileNotFoundException e) {
         e.printStackTrace();
      }
      finally {
         if(fos != null) {
            try {
               fos.close();
            }
            catch(IOException e) {
               // swallowed
            }
         }
      }
   }

   private static Document getUrlWithJSoup(String url) throws IOException {
      return Jsoup.connect(url).
              userAgent("Mozilla").
              data("name", "jsoup").
              timeout(60000).
              maxBodySize(0).get();
   }
}
