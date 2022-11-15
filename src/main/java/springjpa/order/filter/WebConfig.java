package springjpa.order.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {


    @Bean
    public FilterRegistrationBean LogUriFilter() {//필터들간의 순서 조절하기 위해 이방식을 택한다.
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogUriFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 요청이 들어오는 URI 에 대해서 다 적용
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean LoginCheckFilter() {//필터들간의 순서 조절하기 위해 이방식을 택한다.
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 요청이 들어오는 URI 에 대해서 다 적용
        return filterRegistrationBean;
    }




}