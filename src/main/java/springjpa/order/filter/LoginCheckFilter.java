package springjpa.order.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import springjpa.order.controller.session.MemberSession;
import springjpa.order.controller.session.SessionConst;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] WhiteLists = { "/" , "/members/new", "/login" , "/css/*", "/logout"};
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {



        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpSession session= httpRequest.getSession();



        try {
            log.info("인증여부 확인 시작", requestURI);
            if(isLoginCheckPath(requestURI)) {
                if (session == null || (MemberSession) session.getAttribute(SessionConst.MEMBER_SESSION) == null) {
                    log.info(" {} -에 미인증 요청 들어옴", requestURI);
                   HttpServletResponse httpresponse = (HttpServletResponse) response;
                   httpresponse.sendRedirect("/login?redirectURL=" +requestURI);


                    return; //미인증 요청자는 다음으로 진행하지 않고 종료
                }
            }
        chain.doFilter(request, response); //인증 받았거나, 화이트리스트URI에 접속했거나, 다음 체인으로 넘어감
    } catch (Exception e){
                 throw e;
        } finally {
            log.info("인증여부 확인 종료");
     }
}

    private boolean isLoginCheckPath(String requestUri) {
        return !PatternMatchUtils.simpleMatch(WhiteLists,requestUri);
    }

}
