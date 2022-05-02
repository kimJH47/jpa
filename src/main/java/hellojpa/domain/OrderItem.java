package hellojpa.domain;


import javax.persistence.*;

@Entity
public class OrderItem {


    @Id
    @GeneratedValue
    @Column(name = "orderItem_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

/**
 * 연관관계 매핑
 * 객체의 참조와 테이블의 외래 키를 매핑
 *
 * 용어이해
 * 방향(Direction) : 단방향, 양방향
 * 다중성 : 다대일 ,일대다 ,다대다
 * 연관관계 주인 : 객체 양방향 연관관계는 관리주인이 필요
 *
 * 양방향 매핑 규칙
 * 객체의 두관계중 하나를 연관관계 주인으로 지정
 * 연관관계의 주인만이 외래 키를 관리(등록,수정)
 * 주인이 아닌쪽은 읽기만 가능
 * 주인은 mappedBy 속성 사용 X
 * 주인이 아니면 mappedBy 로 주인 지정
 * 외래키가 있는 테이블을 주인으로 정해라!(일대다 관계에서 다)
 * 순수한 객체관계를 고려하면 항상 양족다 값을 입력( 객체관점,테스트케이스 고려), 연관관계 편의메서드 사용하기
 *
 * 단방향 매핑으로도 이미 연관관계 매핑은 완료
 * 양방향 배핑은 반대 방향으로 조회 기능(객체 그래프탐색)이 추가된 것 뿐
 * JPQL 에서 역박향으로 탐색할 일이 많음
 * 단방향 매핑을 잘하고 양방향은 필요할 때 추가해도 됨,객체 입장으로 볼 때 양뱡향 매핑이 좋은것은 아님 (서로 참조하기때문) *
 *
 *
 * */