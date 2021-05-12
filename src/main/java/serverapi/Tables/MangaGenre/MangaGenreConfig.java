package serverapi.Tables.MangaGenre;


import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Genre.Genre;
import serverapi.Tables.Genre.GenreRepository;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.Manga.MangaRepository;


import java.sql.Time;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Configuration
public class MangaGenreConfig {


    @Bean
    MangaGenre commandlineMangaGenre(MangaGenreRepository mangaGenreRepository, MangaRepository mangaRepository, GenreRepository genreRepository) {

//fsdfsdfgsdfsd
//        Optional<Manga> manga = mangaRepository.findById(10L);
//
//        Optional<Genre> genre = genreRepository.findById(39L);
//
//
//
//            Manga getmanga = manga.get();
//            Genre getgenre = genre.get();
//
//
//
//            MangaGenre mangaGenre = new MangaGenre();
//
//            mangaGenre.setGenre(getgenre);
//
//            mangaGenre.setManga(getmanga);
//
//
//            mangaGenreRepository.save(mangaGenre);
//
//            System.out.println("----------------------------------");




        return null;
    }


}
