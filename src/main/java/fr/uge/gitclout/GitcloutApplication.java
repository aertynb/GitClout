package fr.uge.gitclout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Gitclout.
 */
@SpringBootApplication
public class GitcloutApplication {

	/**
	 * Entry point for starting the Gitclout application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
	}
}
