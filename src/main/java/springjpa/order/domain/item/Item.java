package springjpa.order.domain.item;

import lombok.Getter;
import lombok.Setter;
import springjpa.order.exception.NotEnoughStockException;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 하나의 테이블에 세가지 아이템 정보를 다 저장하는 방식을 택함.
@DiscriminatorColumn(name="dtype") // 책,앨범,영화에 대한 구분자
@Getter
@Setter
public abstract class Item { // 추상클래스로 만든다. 구현체를 가지고 주문과 연결

    @Id
    @GeneratedValue
    @Column(name="item_id")
    public Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 비즈니스 로직 , 도메인 주도 설계, 데이터를 가지고 있는 쪽에 비즈니스 로직을 넣는게 가장 객체지향적인 방식이고 응집력이 있다.
    // setter를 사용하지 않고 이 방식 택하는 것이 가장 객체지향적인 방식이다.
    // 엔티티 자체에서 해결 할 수 있는 비즈니스 로직은 엔티티 레벨에서 구현
    /**
     *  재고 관리 기능 - 재고 수량 stock 증가
     * @param stockQuantity
     */
    public void addStock(int stockQuantity){
        this.stockQuantity += stockQuantity;
    }
    /**
     *  재고 관리 기능 - 재고 수량 stock 감소
     * @param stockQuantity
     */
    public void removeStock(int stockQuantity){
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고 수량이 부족합니다.");
        }
        this.stockQuantity = restStock;
    }
}

