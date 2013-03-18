package cs.montclair.softwareeng.model;

public class Bug {

   private int id;
   private String name;
   private String description;
   private VCSCommit commit;

   public Bug() {

   }

   public Bug(int id, String name, String description, VCSCommit commit) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.commit = commit;
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
