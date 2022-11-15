package springjpa.order.repository;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch { // 회원의 이름과 주문상태를 이용한 동적 쿼리 구현을 위한 클래스
    private String memberName;
    private String orderStatus;
}
