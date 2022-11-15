package springjpa.order.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springjpa.order.domain.item.Item;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name="order_item_id")
    public Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;
    private int orderPrice; // 주문가격
    private int count; // 주문수량

    // 유지보수의 편의를 위해 orderItem의 생성방식을 order를 통한 방식만 두고
    // 다른 곳에서 new OrderItem 과 같은 방식으로 생성되는 것을 막으려면 protected 로 두는 것이 좋다.
    // 즉 생성방식 관리가 여러가지로 분산되지 않도록 막는 것이 좋다.
    /*protected OrderItem(){ // jpa 는 protected 까지 기본생성자를 만드는것을 허용
    }@NoArgsConstructor(access = AccessLevel.PROTECTED) 와 동일 */
    /**
     * 생성 메소드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    /**
     * 제품 주문 취소 - 아이템별 취소 수량에 대한 재고 반영
     */
    public void cancel() {
        getItem().addStock(count); // 취소수량만큼 재고수량 원복
    }

    // 조회 로직

    /**
     * 주문상품 전체 가격 조회
     * @return
     */
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
