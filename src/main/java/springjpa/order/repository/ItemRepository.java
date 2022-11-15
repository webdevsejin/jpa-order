package springjpa.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springjpa.order.domain.item.Item;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null){
            em.persist(item); // id 없으면 신규 저장
        } else {
            em.merge(item); // merge(병합) - update와 유사하나 수정이 없는 부분이 null 로 저장되는 항목이 발생할 수 있어서 사용시 주의해야 함.
                            // 병합은 준영속성 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능임.
        }
    }

    public Item findOne(Long id){
        return Optional.ofNullable(em.find(Item.class, id)).get();
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
