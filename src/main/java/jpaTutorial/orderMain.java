package jpaTutorial;

import jpaTutorial.domain.Order;
import jpaTutorial.domain.OrderStatus;
import jpaTutorial.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class orderMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("study_jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            User user = new User("jang", "daegu", "duryu", "22222");
            em.persist(user);

            Order order = new Order();
            order.setStatus(OrderStatus.ORDERED);
            order.setUser(user);
            em.persist(order);

            tx.commit();

            Order order1 = em.find(Order.class, order.getId());
            System.out.println(order1);
//            User user1 = order1.getUser();
//            System.out.println(user1);
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
