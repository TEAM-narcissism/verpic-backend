package teamverpic.verpicbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
//@PropertySource("classpath:/google-cloud.properties")
@ServletComponentScan({"teamverpic.verpicbackend.controller"})
@PropertySource("classpath:/google-cloud.properties")
public class VerpicBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerpicBackendApplication.class, args);
	}

}
