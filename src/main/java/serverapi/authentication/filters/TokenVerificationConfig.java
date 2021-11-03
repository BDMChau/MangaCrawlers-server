package serverapi.authentication.filters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenVerificationConfig {


    @Bean
    public FilterRegistrationBean<TokenVerification> verifyToken(){
        FilterRegistrationBean<TokenVerification> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TokenVerification());
        registrationBean.addUrlPatterns("/api/user/*");
        registrationBean.addUrlPatterns("/api/admin/*");
        registrationBean.addUrlPatterns("/api/notification/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TokenVerificationAdmin> verifyTokenAdmin(){
        FilterRegistrationBean<TokenVerificationAdmin> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TokenVerificationAdmin());
        registrationBean.addUrlPatterns("/api/admin/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }


}
