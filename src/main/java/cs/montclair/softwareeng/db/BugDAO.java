package cs.montclair.softwareeng.db;

import cs.montclair.softwareeng.model.Bug;

public class BugDAO extends GenericDao<Bug>{

   public BugDAO() {
      super(Bug.class.getName());
   }

}
