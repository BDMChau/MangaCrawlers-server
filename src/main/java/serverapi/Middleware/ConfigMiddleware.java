package serverapi.Middleware;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMiddleware {

    @Bean
    public FilterRegistrationBean<TokenVerification> tokenVerification(){
        FilterRegistrationBean<TokenVerification> registrationBean
                = new FilterRegistrationBean<>();

        TokenVerification tokenVerification = new TokenVerification();
        registrationBean.setFilter(tokenVerification);
        registrationBean.addUrlPatterns("/api/user/*");
        return registrationBean;
    }
}