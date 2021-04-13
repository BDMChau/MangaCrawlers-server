package serverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApiApplication {

	public static void main(String[] args) {
		try{
			System.out.println("Server is running at port 4000");

			SpringApplication.run(ServerApiApplication.class, args);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
