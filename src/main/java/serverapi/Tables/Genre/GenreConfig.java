package serverapi.Tables.Genre;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class GenreConfig {

    @Bean
    Genre commandlineGenre(GenreRepository genreRepository){


//
//        for(int i=0;i<3;i++){
//
//            Genre genre = new Genre();
//
//            Scanner sc = new Scanner(System.in);
//            System.out.println("Nhập tên thể loại");
//            genre.setGenre_name(sc.nextLine());
//
//
//            genreRepository.save(genre);
//            System.out.println("------------------------------------");
//
//        }

        return  null;
    }
}
