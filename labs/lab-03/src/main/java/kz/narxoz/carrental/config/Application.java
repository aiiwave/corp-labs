package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// Application lives in config, so we scan the whole root package (persistence, client, ...).
@SpringBootApplication(scanBasePackages = "kz.narxoz.carrental")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(RentalService rentalService) {
        return args -> {
            RentalStatus result = rentalService.move(RentalStatus.REQUESTED, RentalStatus.APPROVED);
            System.out.println("Rental moved to: " + result);
        };
    }
}
