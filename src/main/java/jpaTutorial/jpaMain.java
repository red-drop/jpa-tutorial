package jpaTutorial;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("study_jpa");
        EntityManager em = emf.createEntityManager();

        Member member = new Member();
        member.setId(1L);
        member.setName("User1");
        em.persist(member);
        em.close();
        emf.close();
    }
}
