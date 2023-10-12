package fr.uge.gitclout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })	// Attention à ce paramètre il empêche JPA d'agir (utile tant qu'il n'y pas de base de données)
@RestController
public class GitcloutApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}

	@GetMapping("/home")	// localhost::8080/home 			affiche Git Clout !
	public String hello() {
		return "Git Clout !";
	}

	@GetMapping("/test")	// localhost::8080/test 			affiche Test 1 2 1 2
	public String test() {
		return "Test 1 2 1 2";
	}
}
