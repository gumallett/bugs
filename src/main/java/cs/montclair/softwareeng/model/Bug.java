package cs.montclair.softwareeng.model;

public class Bug {

   private int id;
   private String name;
   private String description;
   private String status;
   private VCSCommit commit;

   public Bug() {

   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public VCSCommit getCommit() {
      return commit;
   }

   public void setCommit(VCSCommit commit) {
      this.commit = commit;
   }

   @Override
   public String toString() {
      return String.format("Bug[%d, %s, %s, %s]", id, name, description, commit);
   }
}
