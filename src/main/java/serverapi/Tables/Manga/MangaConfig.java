package serverapi.Tables.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Author.AuthorRepository;

@Configuration
public class MangaConfig {

    @Bean
    Manga commandLineManga(MangaRepository mangaRepository, AuthorRepository authorRepository) {

//        Manga newManga = new Manga();
//        newManga.setManga_name("horimiya");
//        newManga.setStatus("complete");
//        newManga.setDescription("abc");
//        newManga.setStars(4.5f);
//        newManga.setViews(3000);
//        newManga.setThumbnail("abc url");
//        newManga.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//        newManga.setDate_publication(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//        mangaRepository.save(newManga);


//        Manga newmanga1 = new Manga();
//        newmanga1.setManga_name("Black clover");
//        newmanga1.setStatus("complete");
//        newmanga1.setDescription("abc");
//        newmanga1.setStars(4.5f);
//        newmanga1.setViews(3000);
//        newmanga1.setThumbnail("abc url");
//        newmanga1.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//        newmanga1.setDate_publication(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//        mangaRepository.save(newmanga1);

//        Optional<Author> author = authorRepository.findById(7L);
//
   
            return null;
        }


}
