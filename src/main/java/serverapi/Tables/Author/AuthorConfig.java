package serverapi.Tables.Author;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorConfig {

    @Bean
    Author commandLineAuthor(AuthorRepository authorRepository) {
        Author author1 = new Author("Chau");

        authorRepository.save(author1);
        return null;
    }
}
