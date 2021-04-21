package serverapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public void welcome(){
        System.out.println("Welcome to my manga application!");
        System.out.println("This is server api!");
    }
}
