package serverapi.Tables.Author;

import org.hibernate.sql.Select;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.From;
import java.util.Scanner;

@Configuration
public class AuthorConfig {

    @Bean
    Author commandLineAuthor(AuthorRepository authorRepository) {

//
//        String arr []={"Hiiragiboshi Takumi","Akizuki Sorata","Kusanagi Mizuho","Isayama Hajime","Tabata Yuuki",
//                "Tsutsui Taishi","Inagaki Riichiro - Boichi","Oomori Fujino - Kunieda","Fushimi Tsukasa - Rin",
//                "Mashima Hiro","Type-Moon - Nishiwaki Datto","Miyazaki Yuu","Park Yong-Je","Haruba Negi",
//                "Yamaguchi Mikoto - D.p","Furudate Haruichi","Sagara Sou - Kashi (華 - 師 - )","Ishibumi Ichiei",
//                "Fuyubaru Patra","Yaku Yuuki - Chida Eito","Kaku Yuuji","Akutami Gege","Akasaka Aka","Miyajima Reiji",
//                "Gotouge Koyoharu","Ditama Bow","Mogusu","Yokoyari Mengo","Okayado","Rifujin Na Magonote - Fujikawa Yuka",
//                "Gaehoju - Warpic","Suzuki Nakaba","Komi Naoshi","Misora Riku","Shimesaba - Imaru Adachi","Fuse - Kawakami Taiki",
//                "Sung-Lak Jang","Minazuki Suu","Sukeno Yoshiaki","Aneko Yusagi","Hata Kenjiro","Siu","Saitou Kenji - Nao Akinari",
//                "Take","Watari Wataru"};
//
//
//
//
//        for(int i=0;i< arr.length;i++)
//        {
//            Author author = new Author();
//
//            author.setAuthor_name(arr[i]);
//
//
//            authorRepository.save(author);
//
//
//
//        }
//        System.out.println("-----------------------");


        return null;
    }
}
