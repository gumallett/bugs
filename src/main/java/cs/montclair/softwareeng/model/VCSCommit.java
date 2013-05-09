package cs.montclair.softwareeng.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="COMMITS")
public class VCSCommit {

   @Id
   @GeneratedValue
   private int id;

   @Column(unique = true)
   private String revision;

   @Temporal(TemporalType.TIMESTAMP)
   private Date commitDate;

   @Column(name = "email")
   private String user;

   @Lob
   @Type(type = "org.hibernate.type.TextType")
   private String commitMessage;

   @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
   @JoinTable(name="COMMIT_FILES")
   private List<ChangedFile> files;

   public String getRevision() {
      return revision;
   }

   public void setRevision(String revision) {
      this.revision = revision;
   }

   public String getUser() {
      return user;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public List<ChangedFile> getFiles() {
      return files;
   }

   public void setFiles(List<ChangedFile> files) {
      this.files = files;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public Date getCommitDate() {
      return commitDate;
   }

   public void setCommitDate(Date commitDate) {
      this.commitDate = commitDate;
   }

   public String getCommitMessage() {
      return commitMessage;
   }

   public void setCommitMessage(String commitMessage) {
      this.commitMessage = commitMessage;
   }

   public String toString() {
      return String.format("VCSCommit[%d, %s, %s, %s, %s, %s]", id, revision, commitMessage, user, commitDate, files);
   }
}
