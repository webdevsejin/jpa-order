package springjpa.order.controller.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSession {
    private String loginId;
    private String username;
}
