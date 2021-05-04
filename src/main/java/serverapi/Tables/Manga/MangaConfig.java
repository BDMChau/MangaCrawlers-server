package serverapi.Tables.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import serverapi.Tables.Author.Author;
import serverapi.Tables.Author.AuthorRepository;
import serverapi.Tables.Chapter.ChapterRepository;

import java.util.*;

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
        for (int i = 0; i < 1; i++) {



            Scanner sc = new Scanner(System.in);
            Manga newmanga = new Manga();
            System.out.println("Nhap ten manga: ");
            newmanga.setManga_name(sc.nextLine());

            System.out.println("Nhap trang thai: ");
            newmanga.setStatus(sc.nextLine());

            System.out.println("Nhap mo ta: ");
            newmanga.setDescription(sc.nextLine());

            System.out.println("Nhap sao: ");
            newmanga.setStars(sc.nextFloat());

            System.out.println("Nhap view: ");
            newmanga.setViews(sc.nextInt());

            sc.nextLine();
            System.out.println("Nhap thumbnail: ");
            newmanga.setThumbnail(sc.nextLine());

            newmanga.setDate_publication(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            newmanga.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));



            mangaRepository.save(newmanga);
            System.out.println("----------------------------");
        }


            return null;
        }


}
