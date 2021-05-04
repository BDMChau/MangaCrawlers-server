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


//        String arr[] ={"https://cm.blazefast.co/4c/95/4c9553d74b3a12617e6fb3893698ef75.jpg",
//                "https://cm.blazefast.co/39/b1/39b11eee10a2e854ef25f7fab442c1ce.jpg",
//                "https://cm.blazefast.co/05/a8/05a881317dc03b18441ab8c2a6e4a019.jpg",
//                "https://cm.blazefast.co/8d/98/8d9825accd5c01e04f28267fe1e80f9c.jpg",
//                "https://cm.blazefast.co/66/9c/669c6f2b3ae12f15c2898cc8ee277212.jpg",
//                "https://cm.blazefast.co/39/7f/397f26a290398e2923568a14ecaf75e8.jpg",
//                "https://cm.blazefast.co/0f/b1/0fb1e574994bc8c4af0e2c50b92dc290.jpg",
//                "https://cm.blazefast.co/a9/2c/a92c8a227986225eafcc5a79161ff139.jpg",
//                "https://cm.blazefast.co/41/5f/415f54bb11ba7b9a5a4a101f54d77923.jpg",
//                "https://cm.blazefast.co/4a/36/4a3649877fe7a918b34d4dfdf67d04aa.jpg",
//                "https://cm.blazefast.co/58/d3/58d361d390190fc63cb9a6e1798da343.jpg",
//                "https://cm.blazefast.co/24/aa/24aa1a88e886bae3c396ae0172241e5a.jpg",
//                "https://cm.blazefast.co/d4/0e/d40ec95e3154760f3454d8296a4869cc.jpg",
//                "https://cm.blazefast.co/c3/16/c316283165e7f64a6bd9271d46d8cad5.jpg",
//                "https://cm.blazefast.co/a9/99/a9992adfb2b78a64ebeb0d7d380ede05.jpg",
//                "https://cm.blazefast.co/c2/6c/c26cae69ed6a8e038451697960518490.jpg",
//                "https://cm.blazefast.co/28/9c/289c88c3f6f0715249b07be149f7065a.jpg",
//                "https://cm.blazefast.co/63/a5/63a5d45c060fd3e3ca1e9956c9d01181.jpg",
//                "https://cm.blazefast.co/cb/fd/cbfd89b8724253d176a0e37345cc32ad.jpg",
//                "https://cm.blazefast.co/59/a1/59a1a90a4c9ecf3f1f3f7baad017b77d.jpg",
//                "https://cm.blazefast.co/38/99/389998808416a13da5b9e03258b33374.jpg",
//                "https://cm.blazefast.co/86/af/86afde93405223492ecd550b04d6fa1e.jpg",
//                "https://cm.blazefast.co/36/dd/36dd44237e50ecb13b720c313d9405f3.jpg",
//                "https://cm.blazefast.co/d9/98/d998a38cb537186bddb11762c1ff4a52.jpg",
//                "https://cm.blazefast.co/ab/7b/ab7bab1e4dd5669b77697dc9f5946d75.jpg",
//                "https://cm.blazefast.co/5c/4a/5c4ac28ce94331b956d13d81e2f69287.jpg",
//                "https://cm.blazefast.co/a4/b1/a4b1577af2281560440517d88a2644b5.jpg",
//                "https://cm.blazefast.co/4e/f5/4ef5325f763acd00f251beb6590915c6.jpg",
//                "https://cm.blazefast.co/17/35/17354eb83ab23dc8e7bd4fd18299c104.jpg",
//                "https://cm.blazefast.co/9e/cd/9ecda15b961c054f16b2c215f30c499a.jpg",
//                "https://cm.blazefast.co/68/54/68542bad0e0cf6b240d9c8d9e3c769a5.jpg",
//                "https://cm.blazefast.co/c4/59/c459decfc057fde02d3a3a2c8a17d754.jpg",
//                "https://cm.blazefast.co/96/ce/96ce147beb355b92fbb4dec276671dea.jpg",
//                "https://cm.blazefast.co/8e/b6/8eb69bf95b2c3ef220e967996214e557.jpg",
//                "https://cm.blazefast.co/b1/b3/b1b330ea5efd22dfc9b4effd63d949a3.jpg",
//                "https://cm.blazefast.co/7c/fc/7cfc55c2cf5e7936323ac54b437ef47e.jpg",
//                "https://cm.blazefast.co/38/00/3800f4aa098ef2791f50b00861fc975f.jpg",
//                "https://cm.blazefast.co/ee/cf/eecf73c752015b633d5f5a91be508cab.jpg",
//                "https://cm.blazefast.co/cd/26/cd26e1ffa4703fa203365c4211655fa3.jpg",
//                "https://cm.blazefast.co/04/1c/041c5c7e95e49082057add0cdd0a1e24.jpg",
//                "https://cm.blazefast.co/d2/e2/d2e24244a1dbaca7f8ad811adec123e4.jpg",
//                "https://cm.blazefast.co/38/54/38543315efc4ac516976a4e0af19f773.jpg",
//                "https://cm.blazefast.co/8e/f4/8ef484356d4a61d7bcca6c0cd846221f.jpg",
//                "https://cm.blazefast.co/1b/8e/1b8e68c28fc1d19d8378f9deec1fdea4.jpg",
//                "https://cm.blazefast.co/9b/29/9b29026b42e2e4ece3354925e033265d.jpg",
//                "https://cm.blazefast.co/1f/a7/1fa7e4588016cc66b425d833a015ab67.jpg",
//                "https://cm.blazefast.co/88/ba/88ba449fcbddd43f8f981857fdc3b584.jpg",
//                "https://cm.blazefast.co/ca/ed/caedefa85693df2875842151c16603b9.jpg",
//                "https://cm.blazefast.co/ff/2e/ff2e62926a35cfc571bec72b09135c29.jpg",
//                "https://cm.blazefast.co/d6/2b/d62b1e8b4c60db4d5343b44a1dcd107f.jpg",};
//
//        Optional<Chapter> chapter = chapterRepository.findById(2L);
//        Scanner sc = new Scanner(System.in);
//
//        for(int i=0; i<arr.length; i++){
//
//
//            Chapter getchapter = chapter.get();
//
//
//            ImageChapter imageChapter = new ImageChapter();
//
//            imageChapter.setImgchapter_url(arr[i]);
//
//           imageChapter.setChapter(getchapter);
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
