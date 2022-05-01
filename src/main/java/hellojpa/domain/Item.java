package hellojpa.domain;


import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}


/**
 * 상속관계매핑 전략
 * 조인전략 : 정규화된 방식, Insert 쿼리가 2번나감, 조회시 부모와 조인해서 가져옴,부모의 DTYPE 을 이용해서 자식들 구별
 * 단일테이블 전략 : 성능고려 단순 한테이블을 이용하기 때문에 조회시 insert 쿼리가 1번나감, nyll허용, 조회시 DTYPE으로 구분,기본값
 * 구현클래스마다 테이블 전략 : 부모테이블 없이 부모테이블 필드를 다가지는 자식테이블 사용,부모엔티티는 abstract class(테이블사용x)
 * 서브타입을 명확하게 구분가능,not null 제약조건 사용가능,객체지향 관점으로 부모타입의 PK로  조회를 할 때 모든 테이블을 다조회(union)를 해봐야 하기 때문에 비효율 적이다.
 *
 * DB의 슈퍼타입 서브타입간의 논리모델을 객체 상속,JPA 매핑 으로 지원, 조인전략과 단일테이블의 장단점을 가지고 트레이드오프를 고민한다.
 * 조인을 기본을고 깔고 데이터량이 적고 단순하거나 확정가능성이 없다고 생각되면 단일테이블전략을 선택
 */