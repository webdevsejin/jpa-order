package springjpa.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springjpa.order.domain.item.Book;
import springjpa.order.domain.item.Item;
import springjpa.order.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String createForm(BookForm form){
        Book book = new Book();
        // setter 를 열어놓고 저장하는 방식보다 order 처럼 createBook 메소드로 저장하는 방식을
        // 사용하는 방식이 더 현업에서 많이 사용되니 나중에 수정할 것
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItem();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/edit/{itemId}")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        //model mapper 라이브러리 사용해서 get,set자동으로 하는 방법도 있음.
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/edit/{itemId}")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){
        // 준영속엔티티 수정하는 2가지 방법 - 1) 변경감지(dirty checking) 2) merge
        // 그러나 merge는 값을 주지 않으면 null로 저장될 수 있는 위험이 있기 때문에
        // 1번 방법을 사용하는 것이 훨씬 더 좋다. merge를 가능하면 사용하지 않는것이 좋다.
        // controller에서 엔티티 생성하는 방법과 setter 사용하는 방법보다는
        // service 계층에서 영속상태의 엔티티를 조회하고 엔티티의 데이터를 직접 변경하는 방식이 더 좋다.
        // 그렇게 하면 transaction 커밋 시점에 변경 감지가 실행된다. 이 방식이 유지보수에도 훨씬 좋다.
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

        itemService.updateItem(itemId, form.getName(),form.getPrice(),form.getStockQuantity()); // id가 있기 때문에 repository의 merge가 호출됨.
        return "redirect:/items";
    }
}

