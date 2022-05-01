package hellojpa;

import hellojpa.domain.Item;
import hellojpa.domain.Movie;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.rmi.MarshalledObject;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            Movie movie = new Movie();

            movie.setDirector("A");
            movie.setName("movie");
            movie.setPrice(10000);
            movie.setStockQuantity(10);
            em.persist(movie);
            em.flush();
            em.clear();

            Movie movie1 = em.getReference(Movie.class, 1L);
            System.out.println("is loaded : " + emf.getPersistenceUnitUtil().isLoaded(movie1)); //프록시 초기화확인
            Hibernate.initialize(movie1); //강제초기화
            Movie movie2 = em.find(Movie.class, 1l);
            System.out.println("is loaded : " + emf.getPersistenceUnitUtil().isLoaded(movie1));
            System.out.println(movie1==movie2);
            System.out.println(movie1 instanceof Item);

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
        }finally {
            emf.close();
            em.close();
        }
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
 *
 * 4. 변경감지(dirty checking) : 영속상태의 엔티티의 데이터 변경 시 따로 업데이트 쿼리 없이 트랜잭션 커밋을 하면 jpa 가 업데이트 쿼리를 날려준다.
 * 커밋시 내부적으로 flush() 가 실행되는데 이때 1차캐시에 존재하는 엔티티 스냅샷(최초로 영속성 컨텍스트에 저장된 시점)과 앤티티를 비교한다. 이때 변경이 감지되면
 * 쓰기지연SQL 저장소에 업데이트 쿼리 저장 후 flush, 커밋된다.
 *
 * 5.엔티티 삭제
 *
 * 플러시 : 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
 * - 변경감지
 * - 수정된 엔티티를 쓰기 지연 SQL  저장소에 등록
 * - 쓰기 지연 SQL 저장소의 쿼리를 데이터 베이스에 전송(RUD 쿼리)
 *
 *  em.flush() - 직접 호출
 *  트랜잭션 커밋 - 플러시 자동 호출
 *  JPQL 쿼리 실행 - 자동호출
 *
 *  플러시는 영속성 컨텍스트를 비우지 않음, 영속성 컨텍스트의 변걍내용을 데이터베이스 동기화 하는것 , 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화
 *  하면됨
 *
 *
 *  준영속 상태
 *  - 영속 -> 준영속
 *  - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)
 *  - 영속성 컨텍스트가 제공하는 기능을 사용 못함
 *
 *  방법
 *  - em.detach(entity) : 특정 엔티티만 준영속 상태로 전환
 *  - em.clear() : 영속성 컨텍스트 초기화
 *  - em.close() : 영속성 컨텍스트 종료
* */


/**
 * 프록시객체는 처음 사용할 때 한번만 초기화
 * 객체초기화시 프록시 객체를 통해 실제 엔티티에 접근가능, 프록시객체는 원본 엔티티를 상속받기 때문에 타입 체크시 주의해야함(instanceOf 사용, ==는 상속간의 비교가안됨)
 * 만약 영속성컨텍스트에 엔티티가 있는 상태에서 reference()가 호출되면 프록시가아닌 엔티티 원본을 반환,jpa는 같은 트랜잭션에서는 같은 pk를 가지는 엔티티는 동일성을
 * 보장해야 하기 때문에(참조값 ==비교시 항상 true) 원본을 반환한다. 만약 reference()가 호출되어 프록시객체가 먼저 반환 되었으면 다음 find() 조회할때도 프록시겍체를 한다.
 * 프록시는 영속성컨텍스트를 통해 엔티티를 요청하기 때문에 준영속상태 일때는 예외가 발생한다.
 */