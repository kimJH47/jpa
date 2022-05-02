package hellojpa.domain;

import javax.persistence.*;

@Entity
public class Delivery {


    @Id@GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus deliveryStatus;

    @One+((mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;




}
