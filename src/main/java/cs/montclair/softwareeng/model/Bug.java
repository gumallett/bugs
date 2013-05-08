package cs.montclair.softwareeng.model;

public class Bug {

   private int id;
   private String summary;
   private String description;
   private BugStatus status;
   private BugResolution resolution;
   private String issueType;
   private String priority;
   private VCSCommit commit;

   public Bug() {

   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
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

   public String getPriority() {
      return priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public VCSCommit getCommit() {
      return commit;
   }

   public void setCommit(VCSCommit commit) {
      this.commit = commit;
   }

   @Override
   public String toString() {
      return String.format("Bug[%d, %s, %s, %s, %s]", id, summary, commit, status, resolution);
   }
}
