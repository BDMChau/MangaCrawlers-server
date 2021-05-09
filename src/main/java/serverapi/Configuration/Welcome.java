package serverapi.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Welcome {

    @Bean
    public void welcomeCommand(){

        System.out.println("Welcome to my manga application!");
        System.out.println("Server is running at port 4000");

    }
}
