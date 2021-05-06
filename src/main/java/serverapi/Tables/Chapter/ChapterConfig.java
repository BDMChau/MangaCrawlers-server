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
public class ChapterConfig {





    @Bean
    Chapter commandLineChapter(ChapterRepository chapterRepository, MangaRepository mangaRepository) {


//
//        String arr [] ={"Chapter 10 : An Incident At The Castle Town",
//                "Chapter 9 : The Boy's Vow Version 002",
//                "Chapter 8 : The Protectors",
//                "Chapter 7 : Beast",
//                "Chapter 6 : Go Go, First Misson !!!",
//                "Chapter 5 : The Other Noob",
//                "Chapter 4 : The Black Sheep's Crook",
//                "Chapter 3 : The Road To The Magic Emperor",
//                "Chapter 2 : The Magic Knights Entrance Exam",
//                "Chapter 1 : The Boy's Vow",
//                "Chapter 0: Oneshot: Who Will The World Smile At?"};
//        Optional<Manga> manga = mangaRepository.findById(5L);
//        for(int i=10; i>=0;i--){
//
//            Scanner sc = new Scanner(System.in);
//            Manga getmanga = manga.get();
//
//            Chapter chapter = new Chapter();
//            chapter.setChapter_number(0+i);
//            chapter.setChapter_name(arr[i]);
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
