package springjpa.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.Address;
import springjpa.order.domain.Member;
import springjpa.order.domain.Order;
import springjpa.order.domain.OrderStatus;
import springjpa.order.domain.item.Book;
import springjpa.order.domain.item.Item;
import springjpa.order.repository.OrderRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository repository;

    @Test
    public void 상품주문() throws Exception {
        // given (회원,아이템, 수량 입력 후 주문 생성 필요)
        Member member = new Member();
        member.setUsername("member1");
        member.setAddress(new Address("서울","11-2","11012"));
        em.persist(member);

        Book book = new Book();
        book.setName("springboot");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        // when
        int count=2;
        Long orderId = orderService.order(member.getId(), book.getId(), count);

        // then
        Order order = repository.findOne(orderId);
        assertThat(OrderStatus.ORDER).isEqualTo(order.getStatus());
        assertThat(1).isEqualTo(order.getOrderItems().size());
        assertThat(10000*count).isEqualTo(order.getTotalPrice());
        assertThat(8).isEqualTo(book.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void 상품취소() throws Exception {
        // given
        // when
        // then
    }

    @Test
    public void 재고수량_초과() throws Exception {
        // given
        // when
        // then
    }
}