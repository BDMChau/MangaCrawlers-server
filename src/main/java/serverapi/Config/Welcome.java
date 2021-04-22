package serverapi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Welcome {

    @Bean
    public void welcomeCommand(){
        System.out.println("Welcome to my manga application!");
        System.out.println("This is server api!");
    }
}
