package springjpa.order.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    public Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)  // ORDINAL로 쓰면 중간에 다른 코드 추가될 때 위험하므로 STRING으로 사용할 것
    private DeliveryStatus status; // READY , COMP
}
