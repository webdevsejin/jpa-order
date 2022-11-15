package springjpa.order.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogUriFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter init");
    }

    @Override
    public void destroy() {
        log.info("filter destroy");
    }

    @Override
    public void doFilter
            (ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String requestURI = httpRequest.getRequestURI();
        String uuid =String.valueOf(UUID.randomUUID());
        try {
            log.info("request from : {} , {}",uuid, requestURI);
            chain.doFilter(request,response);
        } catch (Exception e){
            throw e;
        } finally {
            log.info("response from : {} ,  {} " ,uuid, requestURI);
        }
    }
}
