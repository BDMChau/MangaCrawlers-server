package serverapi.Configuration.Middleware;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenVerificationConfig {


    @Bean
    public FilterRegistrationBean<serverapi.Middleware.TokenVerification> verifyToken(){
        FilterRegistrationBean<serverapi.Middleware.TokenVerification> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new serverapi.Middleware.TokenVerification());
        registrationBean.addUrlPatterns("/api/user/*");

        return registrationBean;
    }


}
