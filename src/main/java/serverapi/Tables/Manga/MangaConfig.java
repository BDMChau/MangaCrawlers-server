package serverapi.Tables.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Queries.Repositories.MangaRepos;

@Configuration
public class MangaConfig {

    @Bean
    Manga commandLineManga(MangaRepos mangaRepos) {
      return null;
    }

}
