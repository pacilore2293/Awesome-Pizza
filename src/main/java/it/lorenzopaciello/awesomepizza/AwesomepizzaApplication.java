package it.lorenzopaciello.awesomepizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AwesomepizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomepizzaApplication.class, args);
	}

}
