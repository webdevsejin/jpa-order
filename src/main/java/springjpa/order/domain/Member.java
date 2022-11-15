package springjpa.order.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter  // 실무에서는 getter는 열어두고
@Setter  // setter는 꼭 필요한 경우에만 사용하는 것을 추천
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @NotNull
    @Column(name="login_id")
    private String loginId;
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String email; // email이 동일한 사람은 가입할 수 없음.
    @Embedded  // 내장타입을 포함하고 있음을 알리는 어노테이션, embedded 나 embeddable 둘중 하나만 있어도 됨.
    private Address address;

    @OneToMany(mappedBy = "member")  // 연관관계의 주인이 아니고, order 테이블의 member에 의해 mapping되어짐 즉 읽기전용임을 의미
    private List<Order> orders = new ArrayList<>();
}
