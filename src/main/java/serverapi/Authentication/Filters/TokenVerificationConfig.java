package serverapi.Authentication.Filters;
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
        registrationBean.setOrder(1);
        return registrationBean;
    }


}
