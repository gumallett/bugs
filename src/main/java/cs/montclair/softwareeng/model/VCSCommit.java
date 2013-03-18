package cs.montclair.softwareeng.model;

public class VCSCommit {

   private final String revision;

   public VCSCommit(String revision) {
      this.revision = revision;
   }

   public String getRevision() {
      return revision;
   }

   public String toString() {
      return String.format("VCSCommit[%s]", revision);
   }
}
