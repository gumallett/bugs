package cs.montclair.softwareeng.db;

import cs.montclair.softwareeng.model.Bug;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.List;

public abstract class GenericDao<E> {
   private final SessionFactory sessionFactory;
   private final String clazz;

   public GenericDao(String clazz) {
      Configuration config = new Configuration().configure();
      ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(config.getProperties());
      sessionFactory = config.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
      this.clazz = clazz;
   }

   public <E> E find(int id) {
      Session session = open();
      return (E) session.get(clazz, id);
   }

   public void save(E e) {
      Session session = open();
      session.beginTransaction();

      session.saveOrUpdate(e);

      session.flush();
      session.getTransaction().commit();
   }

   public void saveAll(List<E> entities) {
      Session session = open();
      session.beginTransaction();

      for(E bug : entities) {
         session.saveOrUpdate(bug);
      }

      session.flush();
      session.getTransaction().commit();
   }

   protected Session open() {
      return sessionFactory.getCurrentSession();
   }
}
