package teamverpic.verpicbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan({"teamverpic.verpicbackend.controller"})
public class VerpicBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerpicBackendApplication.class, args);
	}

}
