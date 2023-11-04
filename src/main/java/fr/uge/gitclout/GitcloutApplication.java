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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class GitcloutApplication {

	private final static Logger log = LoggerFactory.getLogger(GitcloutApplication.class);
	private final Cloner repo = new Cloner();

	private final CommiterService commiterService;

	public GitcloutApplication(CommiterService commiterService) {
		this.commiterService = commiterService;
	}

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}

	@GetMapping("/home")	// localhost::8080/home 			affiche Git Clout !
	public String hello() {
		return "Git Clout !";
	}

	@Bean
	@GetMapping("/test")	// localhost::8080/test 			affiche Test 1 2 1 2
	public String test(CommiterService commiterService) {
		return commiterService.findAll().stream()
				.map(Commiter::toString)
				.collect(Collectors.joining(", "));
	}

	@Bean
	public CommandLineRunner demo(CommiterService commiterService) {
		return (args) -> {
			try (var git = repo.initRepository("https://github.com/localsend/localsend.git")) {
				commiterService.addAllCommiter(git);
			}
			/*for (var commiter : commiterService.findAll()) {
				log.info(commiter.toString());
			}*/
		};
	}

}
