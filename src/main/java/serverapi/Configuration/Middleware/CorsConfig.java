package serverapi.Configuration.Middleware;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {


    @Bean
    public FilterRegistrationBean<serverapi.Middleware.Cors> verifyToken(){
        FilterRegistrationBean<serverapi.Middleware.Cors> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new serverapi.Middleware.Cors());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }


}
