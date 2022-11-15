package springjpa.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springjpa.order.domain.item.Book;
import springjpa.order.domain.item.Item;
import springjpa.order.repository.ItemRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    @Transactional
    public void saveItem(Item item){
        repository.save(item);
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity ){
        // findOne으로 가져온 이 객체는 영속성 엔티티이기 때문에
        // 수정사항을 별도로 저장 즉 save나 merge 를 할 필요가 없다.
        // transactional에 의해 commit이 되고 jpa는 flush를 날리고
        // 영속성 엔티티내의 변경사항에 대해 update query 를 db에 보내게 된다.
        // 이 방법이 변경감지 기능을 사용하는 예이다. 이것은 merge와 동일한 것으로 보면 된다.
        Item findItem = repository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItem(){
        return repository.findAll();
    }

    public Item findOne(Long id){
        return repository.findOne(id);
    }
}
