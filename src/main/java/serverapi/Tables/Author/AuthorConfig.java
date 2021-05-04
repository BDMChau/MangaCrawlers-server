package serverapi.Tables.Author;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class AuthorConfig {

    @Bean
    Author commandLineAuthor(AuthorRepository authorRepository) {

//        Scanner sc = new Scanner(System.in);
//        for(int i=0;i<=2;i++)
//        {
//            Author author = new Author();
//            System.out.println("Nhập tên tác giả");
//            author.setAuthor_name(sc.nextLine());
//
//
//            authorRepository.save(author);
//
//
//            System.out.println("-----------------------");
//        }


        return null;
    }
}
