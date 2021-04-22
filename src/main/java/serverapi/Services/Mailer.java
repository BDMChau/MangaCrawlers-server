package serverapi.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class Mailer{
    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void sendMail(String email) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(System.getenv("EMAIL_USERNAME"));

        mail.setSubject("This is title of email");
        mail.setText("text???");

        javaMailSender.send(mail);
    }

}
