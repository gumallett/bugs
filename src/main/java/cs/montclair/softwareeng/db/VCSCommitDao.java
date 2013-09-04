package cs.montclair.softwareeng.db;

import cs.montclair.softwareeng.model.ChangedFile;
import cs.montclair.softwareeng.model.VCSCommit;
import org.hibernate.Session;

public class VCSCommitDao extends GenericDao<VCSCommit> {

   public VCSCommitDao() {
      super(VCSCommit.class.getName());
   }

   public VCSCommit findByRevision(String revision) {
      Session session = open();
      session.beginTransaction();
      VCSCommit commit;

      try {
         commit = (VCSCommit)
            session.createQuery("from VCSCommit as commit where commit.revision=:revision").setString("revision", revision).uniqueResult();
      }
      catch(Exception e) {
         session.getTransaction().rollback();
         session.close();
         return null;
      }

      session.close();

      return commit;
   }
}
