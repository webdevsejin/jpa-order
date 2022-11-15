package springjpa.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springjpa.order.controller.session.MemberSession;
import springjpa.order.controller.session.SessionConst;
import springjpa.order.domain.Member;
import springjpa.order.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginMemberForm";
    }

    //@PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult result, HttpServletResponse response){ // 사이즈나 값 등에 대해 validation 된 내용에 대해 작업
        if (result.hasErrors()){
            return "login/loginMemberForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){ // global 오류 처리
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginMemberForm";
        }

        // 로그인 성공처리 Todo - 쿠키를 만들어서 클라이언트에 전달해줘야 함.

        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료), 쿠키의 이름은 memberId 이고 값은 member_id 임
        //로그인 시 browser의 response header 에 set-cookie:memberId=1 가 있는지 확인
        //그 이후의 request header에 cookie 값이 있는지 확인 / application 의 storage 아래 cookies 확인
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        idCookie.setPath("/");
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("/login") //세션을 활용한 로그인 : httpsession, httpservletrequest 이용
    public String sessionLogin(@Validated @ModelAttribute LoginForm form,
                               @RequestParam(defaultValue = "/") String redirectURL,
                               BindingResult result,
                               HttpServletRequest request){ // 사이즈나 값 등에 대해 validation 된 내용에 대해 작업
        if (result.hasErrors()){
            return "login/loginMemberForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){ // global 오류 처리
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginMemberForm";
        }

        // 로그인 성공처리


        MemberSession memberSession = new MemberSession();
        memberSession.setLoginId(loginMember.getLoginId());
        memberSession.setUsername(loginMember.getUsername());



        HttpSession session = request.getSession(true);//세션이 있으면 있는 세션을 반환,없으면 생성해서 반환
        session.setAttribute(SessionConst.MEMBER_SESSION,memberSession);

        return "redirect:" + redirectURL;
    }

    //@GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    @GetMapping("/logout") //==> 세션을 사용하는 경우의 로그아웃
    public String session(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        return "redirect:/";



    }
}
