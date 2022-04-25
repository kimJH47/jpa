package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Member a = new Member(150L, "A");
            Member b = new Member(160L, "b");
            em.persist(a);
            em.persist(b);

        } catch (Exception e) {
            transaction.rollback();
        }
        emf.close();
        em.close();
    }
}

/**
 * 영속성 컨텍스트
 * jpa에서 가장 중요한 2가지
 * - 객체와 RDB 매핑하기
 * - 영속성 컨텍스트 : 엔티티를 영구저장하는 환경 (애플리케이션과 DB의 중간계층 느낌)
 * <p>
 * 엔티티 생명주기
 * 비영속(new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
 * 영속(managed) : 영속성 컨텍스트에 관리되는 상태
 * 준영속(detached) : 영속성 컨텍스트에 저장되었다가 분리된 상태
 * 삭졔(removed) : 삭제된 상태*
 * <p>
 * 영속성 컨텍스트의 이점
 * 1. 엔티티 조회,1차캐시 : 기본적으로 persist() 수행시 영속성 컨텍스트의 1차캐시에 저장, 엔티티매니저가 조회 수행시 영속성 컨텍스트의 1차캐시저장 공간 먼저 찾음,
 * 만약 없을시 DB 조회후 1차 캐시에 저장
 * 영속성 컨텍스트는 트랜잭션이 끝나면 닫히고 1차캐시는 영속성 컨텍스트에 존재하기 때문에 1차캐시로 얻는 이점은 크지않음
 * <p>
 * 2. 영속 엔티티의 동일성 보장 : 같은 트랜잭션안에서 1차캐시에 저장을 하기 때문에 영속성컨텍스트 내의 엔티티는 동일성을 보장( == 비교), REPEATABLE READ 트랜잭션 격리레벨을
 * DB에서가 아닌 어플리케이션 차원에서 제공
 * <p>
 * 3. 트랜잭션을 지원하는 쓰기지연(LAZY) : 데이터변경은 트랜잭션 커밋을 하는 순간(실제 쿼리가 날라감)에 이뤄진다. 쓰기지연 SQL 저장소 이용
 */