package serverapi.Tables.ImageChapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Chapter.ChapterRepository;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.Manga.MangaRepository;

import java.sql.Time;
import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;
import java.util.TimeZone;
@Configuration
public class ImageChapterConfig {

    @Bean
    ImageChapter commandLineImageChapter(ImageChapterRepository imageChapterRepository, ChapterRepository chapterRepository, MangaRepository mangaRepository)
    {

//        Optional<Chapter> chapter = chapterRepository.findById(1L);
//        Scanner sc = new Scanner(System.in);
//
//        for(int i=0; i<2; i++){
//
//
//            Chapter getchapter = chapter.get();
//
//
//            ImageChapter imageChapter = new ImageChapter();
//            System.out.println("Them URl");
//            imageChapter.setImgchapter_url(sc.nextLine());
//
//           imageChapter.setChapter(getchapter);
//
//            imageChapterRepository.save(imageChapter);
//
//            System.out.println("----------------------------------");
//
//
//        }








        return null;

    }

}
