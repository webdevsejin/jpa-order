package springjpa.order.repository;

import antlr.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.standard.expression.BooleanTokenExpression;
import springjpa.order.domain.Order;
import springjpa.order.domain.OrderStatus;
import springjpa.order.domain.item.Item;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return Optional.ofNullable(em.find(Order.class, id)).get();
    }

    // 검색기능 단순 구현(조건이 항상 둘 다 있을 경우)
 //   public List<Order> findAll(OrderSearch orderSearch) {
//          멤버이름과 주문상태가 둘다 쿼리조건으로 주어지면 아래와 같이 쿼리 작성하면 됨.
    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }
//        return em.createQuery("select o from Order o join o.member m" +
//                        "where o.status = :status" +
//                        "and m.name = :name", Order.class)
//                //.setParameter("status", orderSearch.getOrderStatus())
//                //.setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000) // 최대 1000건을 가져온다.
//                .getResultList();

////       ===> 동적뭐리로 이를 변경해야 한다. 상태값이 있을때 없을때 등등으로 .....
//        // ==> 권장하지 않음
   // }
//    public List<Order> findAllByString(OrderSearch orderSearch) {
//
//        String jpql = "select o from Order o join o.member m";
//        boolean isFirstCondition = true;
//
//        //주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " o.status = :status";
//        }

        //회원 이름 검색
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " m.name like :name";
//        }
//
//        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
//                .setMaxResults(1000);
//
//        if (orderSearch.getOrderStatus() != null) {
//            query = query.setParameter("status", orderSearch.getOrderStatus());
//        }
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            query = query.setParameter("name", orderSearch.getMemberName());
//        }
//
//        return query.getResultList();
//    }
    // 검색기능을 위한 동적쿼리 JPQL ==> Querydsl 라이브러리로 대체
    /**
     * JPA Criteria 로 동적쿼리 해결하는 방식 ==> 이것도 권장하지 않음
     * @param orderSearch
     * @return
     */
    /**
     * JPA Criteria
     */
//    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//        Root<Order> o = cq.from(Order.class);
//        Join<Object, Object> m = o.join("member", JoinType.INNER);
//
//        List<Predicate> criteria = new ArrayList<>();
//
//        //주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
//            criteria.add(status);
//        }
//        //회원 이름 검색
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            Predicate name =
//                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
//            criteria.add(name);
//        }
//
//        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
//        return query.getResultList();
//    }

    // Querydsl 라이브러리로 동적쿼리 구현 ==> 이 방법을 권장함
    // 실무에서는 스프링부트, jpa, 스프링jpa, querydsl 을 이용해서 코드도
    // 간결하게 작성하고 생산성을 극대화하므로 이 조합을 많이 사용함.
//    public List<Order> findAll(OrderSearch orderSearch){
//        QOrder order = Qorder.order;
//        QMember member = QMember.member;
//
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .where(statusEq(orderSearch.getOrderStatus()),
//                        nameLike(orderSearch.getMemberName()))
//                .limit(1000)
//                .fetch();
//    }

//    private BooleanExpression nameLike(String nameCond) {
//        if (StringUtils.hasNext(nameCond)) {
//            return null;
//        }
//        return member.name.like(nameCond);
//    }

//    private BooleanExpression statusEq(OrderStatus statusCond) {
//        if (statusCond == null) {
//            return null;
//        }
//        return order.status.eq(statusCond);
//    }


}
