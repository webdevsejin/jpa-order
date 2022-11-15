package springjpa.order.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter  // 실무에서는 getter는 열어두고
@Setter  // setter는 꼭 필요한 경우에만 사용하는 것을 추천
@Table(name="orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // orderService의 createOrder를 통해서만 order를 생성시키기 위함
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")  // FK가 member_id 가 되는 것이고 이 것을 연관관계의 주인으로 잡는 것이 좋다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order를 persist해주게 되면 orderItems도 강제로 persist 날려줌
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // order를 persist해주게 되면 delivery도 강제로 persist 날려줌
    @JoinColumn(name="delivery_id")
    private Delivery delivery; // order를 가지고 delivery를 찾는 경우가 많다. FK를 order에 둬도 delivery에 둬도 되는데 orders에 두도록 하겠다. 연관관계의 주인을 FK와 가까운 orders에 두었다.

    private LocalDateTime orderDate;  // 날짜에 대해 어노테이션을 별도로 적지 않아도, 이렇게 사용하면 java8에서는 hibernate가 알아서 자동으로 지원을 해줌.
    @Enumerated(EnumType.STRING)  // ORDINAL로 쓰면 중간에 다른 코드 추가될 때 위험하므로 STRING으로 사용할 것
    private OrderStatus status; // 주문의 상태 : [ORDER,CANCEL]

    /**
     * 연관관계 메소드
     */
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    /**
     * 생성 메소드(주문 관련 여러 작업을 한 곳에 응집시켜놓음)
     * 클래스타입... 은 매개변수를 받긴 하지만 몇개인지 모른다라는 뜻임
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
           order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 이 예제에서는 트랜잭션 스크립트 패턴이 아닌
    // 도메인 모델 패턴 방식으로 개발한 것임.
    //비즈니스 로직
    /**
     * 주문취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem:orderItems){
            orderItem.cancel(); // 각 아이템 수량별로 취소 => 재고 반영 해야함.
        }
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem:orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
// ==> 이런방식으로 간편하게 작성하는 방법도 있음
//      return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
    }

}


