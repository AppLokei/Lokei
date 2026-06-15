package Lokei.aplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicationApplication.class, args);
	}

	@Bean
	public CommandLineRunner gerarHash(PasswordEncoder passwordEncoder) {
		return args -> {
			System.out.println(passwordEncoder.encode("Teste123A@"));
		};
	}
}