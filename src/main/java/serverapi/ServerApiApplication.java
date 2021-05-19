package serverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class ServerApiApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(ServerApiApplication.class, args);
        } catch (Exception ex) {
            if(ex.getClass().getName().contains("SilentExitException"))
            {
//                System.out.println(ex);
            } else{
                ex.printStackTrace();
            }

        }

    }
}
