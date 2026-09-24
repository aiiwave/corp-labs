package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Starts the Spring Boot process. Lives in config, together with
 * RentalService and RuleConfig — this is the only package that
 * knows about Spring.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Small demo run so `mvn spring-boot:run` shows something happening:
     * moves a rental request one allowed step and prints the result.
     */
    @Bean
    public CommandLineRunner demo(RentalService rentalService) {
        return args -> {
            RentalStatus result = rentalService.move(RentalStatus.REQUESTED, RentalStatus.APPROVED);
            System.out.println("Rental moved to: " + result);
        };
    }
}
