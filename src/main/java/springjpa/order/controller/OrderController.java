package springjpa.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springjpa.order.domain.Member;
import springjpa.order.domain.Order;
import springjpa.order.domain.item.Item;
import springjpa.order.repository.OrderSearch;
import springjpa.order.service.ItemService;
import springjpa.order.service.MemberService;
import springjpa.order.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;


    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItem();

        model.addAttribute("members",members);
        model.addAttribute("items",items);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                       @RequestParam("itemId") Long itemId,
                       @RequestParam("count") int count) { // form-submit의 name과 value를 받아줄 때 RequestParam 사용
        // 핵심 비즈니스 로직은 식별자만 넘기고 서비스계층에서 영속성 컨텍스트가 존재하는 상태에서
        // 처리하는 것이 좋다.
        orderService.order(memberId,itemId,count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(Model model){
            //@ModelAttribute("orderSearch") OrderSearch orderSearch,
        //List<Order> orders = orderService.findOrders(orderSearch);
        List<Order> orders = orderService.findOrders();
        model.addAttribute("orders", orders);
        return "order/orderList";
    }
}

