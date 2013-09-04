package cs.montclair.softwareeng.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="BUGS")
public class Bug {

   @Id
   private int id;

   private String summary;
   private String issueType;

   @Lob
   @Type(type = "org.hibernate.type.TextType")
   private String description;

   @Temporal(TemporalType.TIMESTAMP)
   private Date createdDate;

   @Enumerated(EnumType.STRING)
   private BugType type;

   @Enumerated(EnumType.STRING)
   private BugStatus status;

   @Enumerated(EnumType.STRING)
   private BugResolution resolution;

   @Enumerated(EnumType.STRING)
   private BugPriority priority;

   @OneToOne(cascade = {CascadeType.ALL})
   private VCSCommit commit;

   public Bug() {

   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public BugType getType() {
      return type;
   }

   public void setType(BugType type) {
      this.type = type;
   }

   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public BugStatus getStatus() {
      return status;
   }

   public void setStatus(BugStatus status) {
      this.status = status;
   }

   public BugResolution getResolution() {
      return resolution;
   }

   public void setResolution(BugResolution resolution) {
      this.resolution = resolution;
   }

   public String getIssueType() {
      return issueType;
   }

   public void setIssueType(String issueType) {
      this.issueType = issueType;
   }

   public BugPriority getPriority() {
      return priority;
   }

   public void setPriority(BugPriority priority) {
      this.priority = priority;
   }

   public Date getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }

   public VCSCommit getCommit() {
      return commit;
   }

   public void setCommit(VCSCommit commit) {
      this.commit = commit;
   }

   @Override
   public String toString() {
      return String.format("Bug[%d, %s, %s, %s, %s, %s, %s, %s]",
         id, summary, commit, status, resolution, priority, issueType, createdDate.toString());
   }
}
