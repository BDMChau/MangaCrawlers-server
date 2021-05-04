package serverapi.Tables.ChapterComments;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Chapter.ChapterRepository;
import serverapi.Tables.User.User;
import serverapi.Tables.User.UserRepository;

import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;
import java.util.TimeZone;

@Configuration
public class ChapterCommentsConfig {



    @Bean
    ChapterComments commandlinechaptercomments(ChapterCommentsRepository chapterCommentsRepository, UserRepository userRepository, ChapterRepository chapterRepository)
    {
//
//        Optional<User> user = userRepository.findById(1L);
//
//        Optional<Chapter> chapter = chapterRepository.findById(1L);
//        Scanner sc = new Scanner(System.in);
//
//        for(int i=0;i<3;i++){
//
//            User getuser = user.get();
//            Chapter getchapter = chapter.get();
//
//            ChapterComments chapterComments = new ChapterComments();
//            System.out.println("Nhập nội dung comment");
//            chapterComments.setChaptercmt_content(sc.nextLine());
//
//            chapterComments.setChaptercmt_time(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//            chapterComments.setUser(getuser);
//            chapterComments.setChapter(getchapter);
//
//
//            chapterCommentsRepository.save(chapterComments);
//
//
//
//            System.out.println("---------------------------");
//
//
//        }














        return  null;
    }


}
