package springjpa.order.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginForm {
    //@NotEmpty(message = "회원이름은 필수입니다.")
    private String loginId;
    private String password;

}

