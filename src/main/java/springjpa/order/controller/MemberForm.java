package springjpa.order.controller;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import springjpa.order.domain.Address;
import springjpa.order.domain.Order;

import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MemberForm {
    //@NotEmpty(message = "회원이름은 필수입니다.")
    private String username; // 반드시 입력받게 하려면?
    private String loginId;
    private String password;
    private String email;
    private String city;
    private String street;
    private String zipcode;
}

