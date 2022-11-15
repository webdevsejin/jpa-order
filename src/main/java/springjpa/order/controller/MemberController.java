package springjpa.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import springjpa.order.domain.Address;
import springjpa.order.domain.Member;
import springjpa.order.service.LoginService;
import springjpa.order.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Validated MemberForm form, BindingResult result){ // 사이즈나 값 등에 대해 validation 된 내용에 대해 작업
                                    // member 를 사용하지 않고 memberForm을 사용하는 이유 --> 화면에 fit 한 별도의 form 을 가져가는 것을 권장한다.
                                    // 특히나 외부로 나가는 api 를 작성하는 경우에는 절대로 entity를 넘겨서는 안된다.
                                    // entity 관련 보안도 문제지만, entity가 변경되는 경우 api spec이 변경될 수 있기 때문이다.
        if (result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        // service 나 repository 단에서 처리하는 것으로 수정 필요
        Member member = new Member();
        member.setAddress(address);
        member.setLoginId(form.getLoginId());
        member.setPassword(form.getPassword());
        member.setEmail(form.getEmail());
        member.setUsername(form.getUsername());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }

}


