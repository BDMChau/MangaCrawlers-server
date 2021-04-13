package serverapi.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class MangaConfig {

    @Bean
    Manga commandLineRunner(MangaRepository mangaRepository) {
        Manga newManga = new Manga(
                "abc123",
                "horimiya",
                "completed",
                "qua hayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",
                4.5f,
                300,
                "abxxyz url",
                Calendar.getInstance(TimeZone.getTimeZone("UTC")),
                Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        );
        mangaRepository.save(newManga);

        return null;
    }

}
