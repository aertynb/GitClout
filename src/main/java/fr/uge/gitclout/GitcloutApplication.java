package fr.uge.gitclout;


import fr.uge.gitclout.gitclone.Cloner;

import fr.uge.gitclout.service.CommitService;
import fr.uge.gitclout.service.CommiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;
import java.util.Objects;


@SpringBootApplication
@RestController
public class GitcloutApplication {

	private final Cloner repo = new Cloner();

	private final CommiterService commiterService;
	private final CommitService commitService;

	public GitcloutApplication(CommiterService commiterService, CommitService commitService) {
		this.commiterService = commiterService;
		this.commitService = commitService;
	}

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}

	@Bean
	public CommandLineRunner cloneRepo(CommiterService commiterService, CommitService commitService) {
		Objects.requireNonNull(commiterService);
		return (args) -> {
			try (var git = repo.initRepository("https://github.com/localsend/localsend.git")) {
				commiterService.addAllCommiter(git);
				//commitService.addAllCommit(git);
			}
			repo.rmFiles(new File("ressources/repo"));
		};
	}

}
