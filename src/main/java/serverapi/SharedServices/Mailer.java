package serverapi.SharedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@EnableAsync
public class Mailer {
    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void sendMailToChangePass(String userName, String userEmail, String token) throws MailException,
            MessagingException {
        String link = System.getenv("CLIENT_POINT_CHANGE_PASS") + "/" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, "utf-8");

        mail.setTo(userEmail);
        mail.setFrom(System.getenv("EMAIL_USERNAME"));
        mail.setSubject("Reset password for MangaCrawlers");

        String htmlMsg = "<h3>Hello " + userName + ", follow this email to reset your password ^^</h3>"
                + "<br/> This link will be expired in 10 minutes "
                + "<a href=" + link + ">Reset password</a>";

        mail.setText(htmlMsg, true);


        javaMailSender.send(mimeMessage);
    }

}
