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


        String arr[] ={      "https://cm.blazefast.co/31/94/319406ca4eabd11878cfbe2da4b44b14.jpg",
                "https://cm.blazefast.co/18/fb/18fb43ed8ca4b89385a2d1963a529eee.jpg",
                "https://cm.blazefast.co/70/a2/70a222edfdb2866c246eed3fed20169e.jpg",
                "https://cm.blazefast.co/19/c8/19c8eeb1b775ce49a3257f50d75177c6.jpg",
                "https://cm.blazefast.co/ed/a1/eda14a7781ddb3b0d4dd0616abf34719.jpg",
                "https://cm.blazefast.co/d5/a4/d5a4ab69b250295ac00a9c73fce96bda.jpg",
                "https://cm.blazefast.co/0f/28/0f28c6d02a70491290f547866f2e057c.jpg",
                "https://cm.blazefast.co/e6/72/e67264fec8f8808e7c58c7cdab14ac77.jpg",
                "https://cm.blazefast.co/a7/d8/a7d84140e62fc15118dc22ea79060417.jpg",
                "https://cm.blazefast.co/c0/dd/c0dd067f953977fd74713b8a0ae8ff80.jpg",
                "https://cm.blazefast.co/e6/99/e69994c40f16a7d20709daa2c84d8072.jpg",
                "https://cm.blazefast.co/17/0b/170b994f9634bc06e259d8176b2e4cf1.jpg",
                "https://cm.blazefast.co/9b/7d/9b7db5d2ad9e47d1775993cdaf9d113c.jpg",
                "https://cm.blazefast.co/bb/d3/bbd3f9487d96e0793324d94890ccb380.jpg",
                "https://cm.blazefast.co/df/7b/df7b9549eb767f21291eb73ec374c5e9.jpg",
                "https://cm.blazefast.co/6a/ac/6aacb5a9edf32789a6eb4d8f2da4a55f.jpg",
                "https://cm.blazefast.co/b6/84/b6848cce0d1b8f65e56d9c6621d4a3fb.jpg",
                "https://cm.blazefast.co/a5/8c/a58c06f562ab0a3e407a283dbb9afb4b.jpg",
                "https://cm.blazefast.co/94/97/9497c79247a68e4017c957dbf78933f8.jpg",
                "https://cm.blazefast.co/29/3b/293ba863a3fcd4997897ac72117b7c9a.jpg",
                "https://cm.blazefast.co/49/b3/49b32c8c9e677e0c048b37eb29550d47.jpg",
                "https://cm.blazefast.co/0f/47/0f471708af68d00c84f292a3a5140e7e.jpg",
                "https://cm.blazefast.co/ad/fc/adfc219ca0f3257c052454c5122c727b.jpg",
                "https://cm.blazefast.co/a2/ba/a2bab00e9b1c7b23a43c694a7d91db1a.jpg",
                "https://cm.blazefast.co/c5/da/c5da3ab4a2a068ab16c34b68d5c61f85.jpg",
                "https://cm.blazefast.co/42/cf/42cf7f4233a0f00e46047d2345f04645.jpg",
                "https://cm.blazefast.co/14/5b/145b3a1b8c357267ef3cfe6e55f719cb.jpg",
                "https://cm.blazefast.co/f4/fd/f4fd2d213d000da34643402c3f3358c0.jpg",
                "https://cm.blazefast.co/0c/23/0c23fec4336cde365536631b468cb759.jpg",
                "https://cm.blazefast.co/71/01/710147554ce42a705325e4261a78b261.jpg",
                "https://cm.blazefast.co/0e/75/0e755b23e531784165ae5f66e5eb46d8.jpg",
                "https://cm.blazefast.co/90/c5/90c5cfd596f5a0e3e348263120cce014.jpg",
                "https://cm.blazefast.co/15/9c/159cd9c7eb09ef07f99e0d26c19e4f8c.jpg",
                "https://cm.blazefast.co/af/f3/aff361ea6018ff380bf767857f3b3333.jpg",
                "https://cm.blazefast.co/15/e0/15e047ebe9146f43475c6c6a3e51231c.jpg",
                "https://cm.blazefast.co/14/4d/144dd340f9f9e024fd9aa01cdbc1ce72.jpg",
                "https://cm.blazefast.co/9d/f2/9df2639c99ebb38bdbeb6f396826151f.jpg",
                "https://cm.blazefast.co/27/9a/279a22a56487fa02385c954ca0801f9f.jpg",
                "https://cm.blazefast.co/95/37/95377da6a3a60aa53f066c79e4d45366.jpg",
                "https://cm.blazefast.co/4d/be/4dbe131fb6f08ae5555f1ed0605cb6fc.jpg",
                "https://cm.blazefast.co/5b/24/5b24b01df38146657e3b709f772cc96f.jpg",
                "https://cm.blazefast.co/00/76/00764b30e8f8880f5099eee3954f4754.jpg",
                "https://cm.blazefast.co/9c/4c/9c4c67e1ebd69a7a0a49059aa39644ee.jpg",
                "https://cm.blazefast.co/91/4b/914b3045691b6dde35cf11a9d442c688.jpg",
                "https://cm.blazefast.co/3f/31/3f313257cd15b7bd0e262698b7d88721.jpg",
                "https://cm.blazefast.co/aa/19/aa1974b07d53f3c64510f53d39c34c80.jpg",
                "https://cm.blazefast.co/e6/90/e6907eeecac974446659499bd0ac6d0f.jpg",
                "https://cm.blazefast.co/aa/63/aa63a127418df6f070b8b040e7f8d301.jpg",
                "https://cm.blazefast.co/93/54/9354a4a24378b07eba8be33deaa03849.jpg",
                "https://cm.blazefast.co/c1/73/c1737aca8138d40e36a0aabd60ee2059.jpg",
                "https://cm.blazefast.co/52/83/5283014df583601632b019f39978c3c4.jpg",
                "https://cm.blazefast.co/ef/e3/efe3d5009461a7f07fe8b53da0ab3cc9.jpg",};

        Optional<Chapter> chapter = chapterRepository.findById(46L);
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<arr.length; i++){


            Chapter getchapter = chapter.get();


            ImageChapter imageChapter = new ImageChapter();

            imageChapter.setImgchapter_url(arr[i]);

           imageChapter.setChapter(getchapter);


            imageChapterRepository.save(imageChapter);


        }
        System.out.println("----------------------------------");








        return null;

    }

}
