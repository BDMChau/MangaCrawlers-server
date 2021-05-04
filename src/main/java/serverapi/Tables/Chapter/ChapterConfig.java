package serverapi.Tables.Chapter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.Manga.MangaConfig;
import serverapi.Tables.Manga.MangaRepository;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class ChapterConfig {





    @Bean
    Chapter commandLineChapter(ChapterRepository chapterRepository, MangaRepository mangaRepository) {


//
//        for(int i=0; i<=10;i++){
//
//
//            Optional<Manga> manga = mangaRepository.findById(1L);
//            Scanner sc = new Scanner(System.in);
//            Manga getmanga = manga.get();
//
//            Chapter chapter = new Chapter();
//            chapter.setChapter_number(0+i);
//            System.out.println("Nhập tên chapter:");
//            chapter.setChapter_name(sc.nextLine());
//            chapter.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//            chapter.setViews(3000);
//            chapter.setManga(getmanga);
//
//
//            chapterRepository.save(chapter);
//
//            System.out.println("-------------------");
//
//        }



        return null;
    }

}
