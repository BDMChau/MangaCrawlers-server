package serverapi.Tables.Chapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Queries.Repositories.ChapterRepos;

@Configuration
public class ChapterConfig {


    @Bean
    Chapter commandLineChapter(ChapterRepos chapterRepos){
        return null;
    }

}
