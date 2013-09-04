package cs.montclair.softwareeng.model;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
public class ChangedFile {

   @Id
   @GeneratedValue
   private int id;

   @Column(unique = true)
   private String fileName;

   private String extension;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getFileName() {
      return fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public String getExtension() {
      return extension;
   }

   public void setExtension(String extension) {
      this.extension = extension;
   }

   @Override
   public String toString() {
      return String.format("ChangedFile[%s, %s, %s]", id, fileName, extension);
   }
}
