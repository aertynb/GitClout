package fr.uge.gitclout;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.gitclone.Cloner;
import fr.uge.gitclout.repository.CommiterRepository;
import fr.uge.gitclout.service.CommiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@SpringBootApplication
@RestController
public class GitcloutApplication {

	private final static Logger log = LoggerFactory.getLogger(GitcloutApplication.class);
	private final Cloner repo = new Cloner("https://github.com/localsend/localsend.git", new File("ressources/repo"));

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}

	@GetMapping("/home")	// localhost::8080/home 			affiche Git Clout !
	public String hello() {
		return "Git Clout !";
	}

	@GetMapping("/test")	// localhost::8080/test 			affiche Test 1 2 1 2
	public List<Commiter> test(CommiterRepository commiterRepository) {
		return commiterRepository.findByLastName("Masterclass");
	}

	@Bean
	public CommandLineRunner demo(CommiterService commiterService) {
		return (args) -> {
			var commit = commiterService.addCommiter(new Commiter("Dave", "Masterclass"));
			log.info(commit.toString());
		};
	}

}
