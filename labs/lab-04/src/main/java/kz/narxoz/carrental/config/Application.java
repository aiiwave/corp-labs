package kz.narxoz.carrental.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Application lives in config, so we scan the whole root package (persistence, client, handler ...).
@SpringBootApplication(scanBasePackages = "kz.narxoz.carrental")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
