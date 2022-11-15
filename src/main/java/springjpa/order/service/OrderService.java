package springjpa.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.Delivery;
import springjpa.order.domain.Member;
import springjpa.order.domain.Order;
import springjpa.order.domain.OrderItem;
import springjpa.order.domain.item.Item;
import springjpa.order.repository.ItemRepository;
import springjpa.order.repository.MemberRepository;
import springjpa.order.repository.OrderRepository;
import springjpa.order.repository.OrderSearch;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문 - 예제의 단순화를 위해 오더 하나에 오더아이템 하나만 하도록 할 예정임.
    /**
     * 주문 생성 및 재고 반영
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findById(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성 (함수의 argument 자동 입력 => control + space)
        Order order = Order.createOrder(member,delivery,orderItem);

        // 주문 저장
        // Order.java 에서 delivery와 orderItem에 cascade all 로 해줬기 때문
        // order민 persist해주면 delivery와 orderItem이 같이 persist 된다.
        // order가 배송과 주문아이템을 관리하는 이 정도의 범위에서만 cascade를 사용해야 함.
        // 배송과 주문아이템은 order 외에는 다른 곳에서는 참조하는 곳이 없고 persit해주는 life cycle이 동일하기 때문에 가능했던 것이다.
        // 이런 상황이 아니면 별도의 repository 만들어서 각각 persist 해주는 것이 맞다.
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    /**
     * 주문 취소 시에 화면에서는 주문번호만 넘어옴
     */
    public void cancelOrder(Long orderId){
        // 주문 entity 조회 후
        Order order = orderRepository.findOne(orderId);
        // 주문을 취소하면 됨. dirtyCheck 즉 변경감지를 통해 jpa 가 자동으로 db에 update를 해줌
        order.cancel();
    }

    // 검색 - 동적 쿼리 활용
    public List<Order> findOrders(){
    //OrderSearch orderSearch){
        return orderRepository.findAll();
        //orderSearch);
    }

}
