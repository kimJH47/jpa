package hellojpa.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderItem {


    @Id
    @GeneratedValue
    @Column(name = "orderItem_id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    private int orderPrice;
    private int count;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
 */