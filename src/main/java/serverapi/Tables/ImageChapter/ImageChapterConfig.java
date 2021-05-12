package serverapi.Tables.ImageChapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Chapter.ChapterRepository;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.Manga.MangaRepository;

import java.sql.Time;
import java.util.*;


@Configuration
public class ImageChapterConfig {

    @Bean
    ImageChapter commandLineImageChapter(ImageChapterRepository imageChapterRepository, ChapterRepository chapterRepository, MangaRepository mangaRepository)
    {



//        String arr[] ={
//                "https://cm.blazefast.co/ad/2a/ad2a6e5c9dd745be663402a9cc275e46.jpg",
//                "https://cm.blazefast.co/b0/4b/b04b6a6a67277959a063e3f93d20205c.jpg",
//                "https://cm.blazefast.co/67/da/67da1b3e41d05416a7a01e3abc61a917.jpg",
//                "https://cm.blazefast.co/20/a9/20a9e2e904babb18a184b2ce3e7b573d.jpg",
//                "https://cm.blazefast.co/eb/37/eb37ec425d852c94a9deee3c9e99e649.jpg",
//                "https://cm.blazefast.co/12/2b/122b1066c95d78d6d88efa3b959f17b9.jpg",
//                "https://cm.blazefast.co/b7/f3/b7f339a118df54ac1eab29bf7b653d60.jpg",
//                "https://cm.blazefast.co/1a/4c/1a4c15b40152a2206f195a3e6014e4aa.jpg",
//                "https://cm.blazefast.co/be/a0/bea0e4c0d3625194c9e9d1b310689a6c.jpg",
//                "https://cm.blazefast.co/10/6a/106a6949b4046e4aca297ca0aed9d245.jpg",
//                "https://cm.blazefast.co/43/23/43238fc48f92d30af3410ce7cb573dc7.jpg",
//                "https://cm.blazefast.co/af/67/af67cdbf95e49181d0b374ed37f9a12a.jpg",
//                "https://cm.blazefast.co/52/af/52affbae661d5a124f496edcd6dec827.jpg",
//                "https://cm.blazefast.co/29/9f/299f38d4045435f598a66f428f54e69d.jpg",
//                "https://cm.blazefast.co/9d/e2/9de2be1eb714727346986358b04ded73.jpg",
//                "https://cm.blazefast.co/7e/29/7e299c4e554349d2bafc82e3a7f31c26.jpg",
//                "https://cm.blazefast.co/63/83/63836deb4a70d18207655ab6d0ce472b.jpg",
//                "https://cm.blazefast.co/61/36/613641ea03cc836e6fac59e440013fec.jpg",
//                "https://cm.blazefast.co/8b/92/8b9216487c2408df910aa25a1b195944.jpg",
//                "https://cm.blazefast.co/b2/8f/b28fc72b0b0ac3a63d2d4d3230dee2ab.jpg",};
//
//        Optional<Chapter> chapter = chapterRepository.findById(193L);
//       // Scanner sc = new Scanner(System.in);
//        Chapter getchapter = chapter.get();
//
//        for(int i=0; i<arr.length; i++){
//
//
//
//
//
//            ImageChapter imageChapter = new ImageChapter();
//
//            imageChapter.setImgchapter_url(arr[i]);
//
//            imageChapter.setChapter(getchapter);
//
//
//            imageChapterRepository.save(imageChapter);
//
//
//        }
//        System.out.println("----------------------------------");
//
//
//






        return null;

    }

}
