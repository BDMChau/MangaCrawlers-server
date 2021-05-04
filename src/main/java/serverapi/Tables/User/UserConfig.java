package serverapi.Tables.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class UserConfig {

    @Bean
    User commandLineUsers(UserRepository userRepository){

//        for(int i=0;i<1;i++){
//            User user = new User();
//            Scanner sc = new Scanner(System.in);
//            System.out.println("Nhập username:");
//            user.setUser_name(sc.nextLine());
//            System.out.println("Nhập email:");
//            user.setUser_email(sc.nextLine());
//            System.out.println("Nhập password:");
//            user.setUser_password(sc.nextLine());
//            System.out.println("Thêm avatar:");
//            user.setUser_avatar(sc.nextLine());
//            System.out.println("Admin là :");
//            user.setUser_isAdmin(sc.hasNextBoolean());
//
//            userRepository.save(user);
//            System.out.println("-------------------------------");
//
//
//        }





       return null;
    }
}
