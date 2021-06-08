package serverapi.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
@EnableScheduling
public class GlobalConfiguration {


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


    //////////// auto call http every 25 minutes to wake up app on heroku
    @Scheduled(fixedRate = 1500000)
    public void autoCallHttp() throws IOException {
        URL url = new URL(System.getenv("HOST_PRODUCTION") + "api/auth/autocallhttp");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        con.getResponseCode();
    }



}
