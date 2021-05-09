package serverapi.Tables.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MangaConfig {

    @Bean
    Manga commandLineManga(MangaRepository mangaRepository) {
      return null;
    }

}
