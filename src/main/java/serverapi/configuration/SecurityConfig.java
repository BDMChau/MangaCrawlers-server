package serverapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable();

        http
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/api/auth/oauthgooglesigninsusscess")
//                .failureUrl("/api/auth/oauthgooglesigninfailed")
                .clientRegistrationRepository(clientRegistrationRepository)
                .authorizedClientService(authorizedClientService);

    }






}