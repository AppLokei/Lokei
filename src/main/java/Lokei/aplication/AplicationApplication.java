package Lokei.aplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicationApplication.class, args);
	}
}