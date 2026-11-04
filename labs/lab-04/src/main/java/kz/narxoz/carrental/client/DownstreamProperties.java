package kz.narxoz.carrental.client;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** app.downstream.* from yaml. Lab 6 makes the call and enforces the timeout; here it is only bound. */
@ConfigurationProperties(prefix = "app.downstream")
public record DownstreamProperties(String baseUrl, Duration timeout) {
}
