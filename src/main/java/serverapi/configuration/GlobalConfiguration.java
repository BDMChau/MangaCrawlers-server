package serverapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import serverapi.authentication.RegistrationOauth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class GlobalConfiguration {
    private static List<String> clients = Arrays.asList("google");


    private RegistrationOauth registrationOauth;

    @Autowired
    public GlobalConfiguration(RegistrationOauth registrationOauth){
        this.registrationOauth = registrationOauth;
    }

    @Bean
    public void greeting() {
        System.out.println("Welcome to my manga application!");
        System.out.println("Server is running at port 4000");
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());


        return jsonConverter;
    }


    ////////////////////// OAuth
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(client -> registrationOauth.getRegistration(client))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {

        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
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
