package serverapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import serverapi.authentication.RegistrationOauth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class GlobalConfig {
    private static List<String> clients = Arrays.asList("google");


    private RegistrationOauth registrationOauth;

    @Autowired
    protected GlobalConfig(RegistrationOauth registrationOauth) {
        this.registrationOauth = registrationOauth;
    }

    @Bean
    protected void greeting() {
        System.out.println("Welcome to my manga application!");
        System.out.println("Server is running at port 4000");
    }

    @Bean
    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());


        return jsonConverter;
    }

    ////////////////////// OAuth
    @Bean
    protected ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(client -> registrationOauth.getRegistration(client))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    protected OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
    }


    @Bean
    public HttpLogFormatter httpFormatter() {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        return new JsonHttpLogFormatter(mapper);
    }

    //////////// auto call http every 25 minutes to wake up app on heroku
//    @Scheduled(fixedRate = 1500000)
//    public void autoCallHttp() throws IOException {
//        URL url = new URL(System.getenv("HOST_PRODUCTION") + "api/auth/autocallhttp");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        con.connect();
//        con.getResponseCode();
//    }


}
