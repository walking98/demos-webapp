import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;

import wiseking.test.entity.UserEntity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {

            UserEntity user = new UserEntity();
            user.setUserName("wiseking");
            user.setPassword("test");
            // user.setId(15);
            Transaction transaction = session.beginTransaction();

            session.save(user);

            transaction.commit();
            user.setUserName("wiseking1");
            user.setPassword("test1");
            // user.setId(13);
            transaction = session.beginTransaction();
            session.save(user);

            transaction.commit();

            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                System.out.println("entityName: " + entityName);
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("aaa  " + ((UserEntity) o).getUserName());
                }
            }
        } finally {
            session.close();
        }
    }
}