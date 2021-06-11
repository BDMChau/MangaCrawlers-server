package serverapi.Tables.Author;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Query.Repository.AuthorRepos;

@Configuration
public class AuthorConfig {

    @Bean
    Author commandLineAuthor(AuthorRepos authorRepos) {

        return null;
    }
}
