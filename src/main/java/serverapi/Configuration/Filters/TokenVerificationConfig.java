package serverapi.Configuration.Filters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenVerificationConfig {


    @Bean
    public FilterRegistrationBean<serverapi.Filters.TokenVerification> verifyToken(){
        FilterRegistrationBean<serverapi.Filters.TokenVerification> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new serverapi.Filters.TokenVerification());
        registrationBean.addUrlPatterns("/api/user/*");
        registrationBean.addUrlPatterns("/api/admin/*");

        return registrationBean;
    }


}
