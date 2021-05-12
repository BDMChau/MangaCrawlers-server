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



//        String arr [] ={
//
//                "Chapter 10 : Armor Maga",
//                "Chapter 9 : Dear Kaby",
//                "Chapter 8 : Lucy Vs The Duke Of Evaroo",
//                "Chapter 7 : The Weak Point Of Maga",
//                "Chapter 6 : Infiltrating The Duke Of Evaroo S Mansion",
//                "Chapter 5 : Day Break",
//                "Chapter 4 : Stellar Spirit Of The Canis Minor",
//                "Chapter 3 : The Sala,ander, The Monkey, And The Bull",
//                "Chapter 2 : The Master Appears",
//                "Chapter 1 : Fairy Tail",};
//
//
//        Optional<Manga> manga = mangaRepository.findById(10L);
//        for(int i=9; i>=0;i--){
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
//
//
//        }
//        System.out.println("-------------------");



        return null;
    }

}
