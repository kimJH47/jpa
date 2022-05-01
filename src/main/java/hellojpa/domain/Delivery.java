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

    @OneToOne(mappedBy = "delivery")
    private Order order;




}
