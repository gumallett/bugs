package cs.montclair.softwareeng.html.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class XMLUtils {

   /**
    * Parses an XML Document from a text file of presumably valid XML.
    *
    * @param file Input file.
    * @throws org.xml.sax.SAXException if the XML is not valid
    * @throws IOException if the file cannot be found/opened.
    * @throws javax.xml.parsers.ParserConfigurationException
    */
   public static Document getXML(File file) throws SAXException, IOException, ParserConfigurationException {
      ByteArrayInputStream bais = new ByteArrayInputStream(toByteArrayUTF8(file));

      return getXML(bais);
   }

   public static Document getXML(InputStream is) throws SAXException, IOException, ParserConfigurationException {
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringComments(true);
      factory.setNamespaceAware(false);
      factory.setValidating(false);

      final DocumentBuilder builder = factory.newDocumentBuilder();

      return builder.parse(is);
   }

   /**
    * Converts a text file to a byte array using the UTF-8 charset. This method will ignore lines starting with
    * "<!".
    *
    * @param file Input file.
    * @throws IOException if the file does not exist or cannot be opened.
    */
   public static byte[] toByteArrayUTF8(File file) throws IOException {
      final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

      String line;
      final StringBuilder source = new StringBuilder();

      while((line = br.readLine()) != null) {
         // Having issues with the input file. Ignoring comments to prevent SAXException parsing input file.
         if(line.startsWith("<!")) {
            continue;
         }

         source.append(line);
         source.append("\n");
      }

      return toByteArrayUTF8(source.toString());
   }

   public static byte[] toByteArrayUTF8(String s) {
      try {
         return s.getBytes("utf-8");
      }
      catch(UnsupportedEncodingException e) {
         //unlikely
         e.printStackTrace();
      }

      return null;
   }

   /**
    * Returns the number of direct children of input Node.
    *
    * @param node
    */
   public static int getNumChildren(Node node) {
      return node.getChildNodes().getLength();
   }

   /**
    * Returns the maximum depth of child nodes of the input node within the DOM tree.  Elements with no child
    * elements should have a depth of 0.
    *
    * @param node Input Node
    */
   public static int getChildNodeLevels(Node node) {
      if(node.hasChildNodes()) {
         int maxChildNodes = 0;
         NodeList children = node.getChildNodes();

         for(int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if(child.getNodeType() == Node.ELEMENT_NODE) {
               int childLevels = 1 + getChildNodeLevels(child);

               if(childLevels > maxChildNodes) {
                  maxChildNodes = childLevels;
               }
            }
         }

         return maxChildNodes;
      }

      return 0;
   }
}