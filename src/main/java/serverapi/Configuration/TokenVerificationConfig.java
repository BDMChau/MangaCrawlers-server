package serverapi.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Middleware.TokenVerification;

@Configuration
public class TokenVerificationConfig {


    @Bean
    public FilterRegistrationBean<TokenVerification> verifyToken(){
        FilterRegistrationBean<TokenVerification> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TokenVerification());
        registrationBean.addUrlPatterns("/api/user/*");

        return registrationBean;
    }


}
