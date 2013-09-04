package cs.montclair.softwareeng.db;

import cs.montclair.softwareeng.model.Bug;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<E> {

   private static final Logger LOG = LoggerFactory.getLogger(GenericDao.class);

   private static final SessionFactory sessionFactory;

   static {
      Configuration config = new Configuration().configure();
      ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(config.getProperties());
      sessionFactory = config.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
   }

   private final String clazz;

   public GenericDao(String clazz) {
      this.clazz = clazz;
   }

   public <E> E find(int id) {
      Session session = open();
      E entity = null;

      try {
         entity = (E) session.get(clazz, id);
      }
      catch(Exception ex) {
         LOG.error(ex.getMessage(), ex);
      }
      finally {
         session.close();
      }

      return entity;
   }

   public void save(E e) {
      Session session = open();
      session.beginTransaction();

      try {
         session.save(e);
         session.flush();
      }
      catch(Exception ex) {
         LOG.error(ex.getMessage(), ex);
         session.getTransaction().rollback();
         session.close();
         return;
      }

      session.getTransaction().commit();
      session.close();
   }

   public void saveAll(List<E> entities) {
      Session session = open();
      session.beginTransaction();

      int i=0;
      for(E bug : entities) {
         try {
            session.saveOrUpdate(bug);
            session.flush();
         }
         catch(Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
         }

         i++;

         /*if(i % 100 == 0) {
            session.flush();
            session.clear();
            LOG.debug(i + " bugs saved.");
         }*/
      }


      session.getTransaction().commit();
      session.close();
   }

   protected Session open() {
      return sessionFactory.openSession();
   }
}
