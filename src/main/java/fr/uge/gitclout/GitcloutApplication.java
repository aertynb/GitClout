package fr.uge.gitclout;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class GitcloutApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}

}
