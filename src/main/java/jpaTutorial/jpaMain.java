package jpaTutorial;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("study_jpa");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setId(5L);
            member.setName("gang");
            em.persist(member);
            tx.commit();

            Member m = em.find(Member.class, 1L);
            System.out.println("MemberId: " + m.getId());
            System.out.println("MemberName: " + m.getName());
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }
}
