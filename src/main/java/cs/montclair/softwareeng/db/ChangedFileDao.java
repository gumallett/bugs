package cs.montclair.softwareeng.db;

import cs.montclair.softwareeng.model.ChangedFile;
import org.hibernate.Session;

public class ChangedFileDao extends GenericDao<ChangedFile> {

   public ChangedFileDao() {
      super(ChangedFile.class.getName());
   }

   public ChangedFile findByFileName(String filename) {
      Session session = open();
      session.beginTransaction();
      ChangedFile changedFile;

      try {
         changedFile = (ChangedFile)
            session.createQuery("from ChangedFile as file where file.fileName=:name").setString("name", filename).uniqueResult();
      }
      catch(Exception e) {
         session.getTransaction().rollback();
         session.close();
         return null;
      }

      session.close();

      return changedFile;
   }
}
