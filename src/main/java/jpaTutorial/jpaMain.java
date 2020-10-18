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
            member.setId(111L);
            member.setName("gang");

            em.persist(member); // register in persistence context
            /*
                em.setFlushMode(FlushModeType.COMMIT)
                * FlushModeOption
                    - AUTO: [flush, commit]
                    - COMMIT: commit

                flush does not clear persistence context
             */
            em.flush(); // force executing query
            // em.detach(member); // remove from persistence context
            tx.commit(); // execute insert query

            // update ( Dirty check )
            Member m = em.find(Member.class, 6L);
            m.setName("JpaMember");
            m.setId(10L);
            tx.commit();

            if ( m == null) {
                System.out.println("null");
            }else {
                System.out.println("MemberId: " + m.getId());
                System.out.println("MemberName: " + m.getName());
            }
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        emf.close();
    }
}
