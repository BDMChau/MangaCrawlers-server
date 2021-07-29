package serverapi.Auth;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import serverapi.Authentication.Service.AuthService;

@TestConfiguration
public class authServiceConfiguration {

    @Bean
    public AuthService authServiceTest(){
        return new AuthService();
    }

}
