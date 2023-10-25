package fr.uge.gitclout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@SpringBootApplication
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
	public long test(CommiterRepository commiterRepository) {
		return 2;
	}

	@Bean
	public DataSource dataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.sqlite.JDBC");
		dataSourceBuilder.url("jdbc:sqlite:database.db");
		return dataSourceBuilder.build();
	}
}
