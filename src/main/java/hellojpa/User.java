package hellojpa;


import javax.persistence.*;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@SequenceGenerator(
        name = "member_seq",
        sequenceName = "member_seq"
)
@TableGenerator(
        name = "user_seq",
        pkColumnValue = "seq_name"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @ManyToOne
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getUsers()
            .add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {

    }


}


/**
 * 아이덴티티 전략의 특징 : PK 전략을 DB에 위임하기 때문에(auto increment)
 * insert 될때 엔티티의 Pk가 정해진다. 그러나 기본적으로 persist()를 사용시
 * 엔티티는 바로 DB에 저장되는게 아닌 1차캐시에 저장되었다가 트랜잭션 커밋시점에 저장되는데 이때 엔티티에 pk가 존재하지 않게 되므로 아이덴티티 전략은
 * 예외적으로 persist() 가 호출되면 바로 insert 쿼리가 날라간다.
 *
 *
 * 시퀀스 전략 특징 : persist 시 시퀀스 전략이면 DB 시퀀스 객체에 pk 값을 얻어서 영속화 시킨다. insert 쿼리는 트랜잭션 커밋시점에 날라가기
 * 때문에 버퍼를 이용해 한번에 쿼리를 날릴 수 있다. 한번 persist 할때마다 시퀀스 pk값 을 하나가져 오는데 이때 allocationSize 를 이용해 pk를 미리 여러개
 * 받아서 사용이 가능하다( 한번에 하나씩 가져오는 것 보다 미리 여러개 받아서 어플리케이션 내에서 저장해놨다가 쓰는게 성능적으로 좋기때문),테이블전략도 유사하게 동작
 *
 *
 */