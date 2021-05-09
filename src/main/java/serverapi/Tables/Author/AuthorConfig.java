package serverapi.Tables.Author;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorConfig {

    @Bean
    Author commandLineAuthor(AuthorRepository authorRepository) {

        return null;
    }
}
