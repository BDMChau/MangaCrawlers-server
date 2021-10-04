package serverapi.sharing_services;

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
        mail.setSubject("Reset password");

        String htmlMsg = "<h3>Hello " + userName + ", follow this email to change your password ^^</h3>"
                + "<br/> This link will be expired in 10 minutes  "
                + "<h3> <a href=" + link + ">Reset password</a> </h3>" + "<br/> If you have any questions, please let" +
                " " +
                "us know via mangacrawlers123@gmail.com" + "<br/> Thank You!";

        mail.setText(htmlMsg, true);


        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendMailToVerifyAccount(String userName, String userEmail, String token) throws MailException,
            MessagingException {
        String link = System.getenv("CLIENT_POINT_VERIFY_ACCOUNT") + "/" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, "utf-8");

        mail.setTo(userEmail);
        mail.setFrom(System.getenv("EMAIL_USERNAME"));
        mail.setSubject("Verify your account");

        String htmlMsg = "<h3>Hello " + userName + ", follow this email to verify your account ^^</h3>"
                + "<br/> Your account will be deprecated after 3 days if you don't confirm this email  "
                + "<h3> <a href=" + link + ">Verify Account</a> </h3>" + "<br/> After verified, if you can't login, " +
                "it's mean your account being disapproved, contact administrator to confirm and reopen your account " +
                "<br/>" + "If you have any questions, please let us know via mangacrawlers123@gmail.com" + "<br/> Thank " +
                "You!";

        mail.setText(htmlMsg, true);


        javaMailSender.send(mimeMessage);
    }
}
