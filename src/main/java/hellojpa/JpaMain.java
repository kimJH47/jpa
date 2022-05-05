package hellojpa;

import hellojpa.domain.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.rmi.MarshalledObject;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try
        {
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setName("김재현");
                member.setAge(i);
                member.setAddress(new Address("city","test",1));
                em.persist(member);
            }


            List<Member> resultList1 = em.createQuery("select m from Member m order by m.age desc ", Member.class)
                                         .setFirstResult(2)
                                         .setMaxResults(10)
                                         .getResultList();

            int size = resultList1.size();
            System.out.println(size);

            for (Member member1 : resultList1) {
                System.out.println(member1.getAddress());
                System.out.println(member1.getAge());
            }


           transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
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
 * 삭졔(removed) : 삭제된 상태
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
 *  JPQL, 쿼리 실행 - 자동호출
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
 /


/**
 * 프록시객체는 처음 사용할 때 한번만 초기화
 * 객체초기화시 프록시 객체를 통해 실제 엔티티에 접근가능, 프록시객체는 원본 엔티티를 상속받기 때문에 타입 체크시 주의해야함(instanceOf 사용, ==는 상속간의 비교가안됨)
 * 만약 영속성컨텍스트에 엔티티가 있는 상태에서 reference()가 호출되면 프록시가아닌 엔티티 원본을 반환,jpa는 같은 트랜잭션에서는 같은 pk를 가지는 엔티티는 동일성을
 * 보장해야 하기 때문에(참조값 ==비교시 항상 true) 원본을 반환한다. 만약 reference()가 호출되어 프록시객체가 먼저 반환 되었으면 다음 find() 조회할때도 프록시겍체를 한다.
 * 프록시는 영속성컨텍스트를 통해 엔티티를 요청하기 때문에 준영속상태 일때는 예외가 발생한다.
 *
 * cascade.ALL 사용시 주의점 : 부모엔티티와 자식엔티티 라이프사이클이 유사할때 사용, 자식엔티티에 대해 소유자가 하나일때 사용해야함
 * 고아객체 : orphanRemoval=true 사용시 부모엔티티와의 관계가 끊어진 자식엔티티(참조가 되지않는 엔티티)는 delete 쿼리로 자동 삭제된다(부도엔티티가 삭제되면 자식도삭제_)
 * . 참조하는 곳이 하나일 때
 * 사용해야함, 특정 엔티티가 개인 소유할 때 사용
 */


/**
 * 기본값 타입
 * String,int,Integer 등 자바에서 제공하는 값 타입
 * 엔티티의 생명주기에 의존한다 : 회원을 삭제하면 회원의 나이나 이름 필드가 함께 삭제된다
 * 값 타입은 공유하면 안된다 : 어떤 회원의 나이를 변경하는데 다른 회원의 나이가 변경되서는 안된다(Side Effect).자바의 기본타입은 공유되지 않는다(래퍼클래스는 공유는 되지만 변경이 안되기 때문에
 * 사이드이펙트가 발생하지 않는다).
 *
 * 임베디드 타입
 * 기본값 타입들을 복합적으로 묶어서 하나로 사용하는 것, 재사용 과 높은 응집도, 값타만 사용하는 의미있는 메서드르 작성가능, 엔티티에 생명주기 의존
 * 임베디드 타입을 중복해서 사용할 때 @AttributeOverride(s) 어노테이션 사용
 *
 * 값 타입 공유 참조
 * 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함(Side Effect), 값을 복사해서 사용해야함.
 * 한계점 : 항상 값 복사을 해서 사용하면 사이드이펙트를 피할 순 있다. 하지만 기본타입이 아닌 임베디드타입 처럼 직접 정의한 값타입은 객체타입이다. 그래서 참조값을 복사하는 것을 컴파일러단계에서
 * 막을수 없다.(NewAdress = member.getAdress() // 아무문제없이 컴파일됨)
 *
 * 불변객체
 * 값타입 공유 참조 문제점을 막기위해 객체타입을 생성이후 수정 할 수 없는 불변객체로 만들어야한다.(setter 사용 x 등)
 * 실제 변경을 하기위해선 새로운 객체를 생성해서 할당해야한다
 *
 * 값 타입 비교
 * 동일성(identity) 비교 : 인스턴스의 참조값 비교 , == 비e교
 * 동등성(equivalence) 비교 : 인스턴스의 값을 비교, equals() 재정의 해서 사용 (hashCode())
 *
 * 값타입 컬렉션
 * 값타입 컬렉션들은 전부 지원로딩된다. 영속성 전이 + 고아 객체 제거 기능을 필수로 가진다.
 * 값타입은 기본적으로 수정하며 안되며(불변) 변경시 새로운 인스턴스로 갈아끼워야한다.
 * 컬랙션도 마찬가지로 기존 컬랙션이 가지고있는 인스턴스를 삭제 후 새로운 인스턴스를 넣어야한다. 이때 equals()와 hashCode()를 재대로
 * 구현해서 사용해야한다.
 *
 * 값 타입 컬렉션의 제약사항
 * 값 타입은 엔티티와 다르게 식별자 개념이 없다.
 * 값은 변경하면 추적이 어렵다.
 * 값 타입 컬렉션에 변경 사항이 발생하면 주인 엔티티와 연관된 모든 데이터를 삭제하고 현재 값을 모두 다시 저장한다(insert 쿼리)
 * 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야함 : null X, 중복 X
 *
 * 값 타입 컬렉션 대안
 * 값 타입 컬렉션 대신 일대다 관계 고려
 * 영속성 전이 와 고아객체 제거를 사용해서 값타입 컬렉션처럼 사용
 *
 * */

/**
 * JPQL
 * 엔티티 객체를 대상으로 쿼리, SQL을 추상화해서 특정DB SQL에 의존하지않고 컴파일 후 SQL 방언에 맞춰 변환됨
 *
 * TypeQuery : 반환타입이 명확할 때 사용
 * Query : 반환 타입이 멸확하지 않을 때 사용(예:조회컬럼이 2개 이상 일때) :
 *  1. Object 로 반환된다(타입캐스팅해서 사용해야함,List<Object[]> 타입캐스팅
 *  2. new 를 이용해서 Dto 객체랑 바인딩(패키지명을 전체 입력해줘야함, 순서와 타입이 일치해야함)
 *
 * 프로젝션
 * select 절에 조회할 대상을 지정 하는것
 * 대상 : 엔티티,임베디드 타입, 스칼라 타입(기본 데이터타입)
 * 엔티티 프로젝션 : JPQL 로 반환된 모든 엔티티들은(조인해서 찾은 엔티티 포함) 영속성 컨텍스트에서 관리된다.
 * */