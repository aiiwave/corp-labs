package kz.narxoz.carrental.config;

import kz.narxoz.carrental.client.DownstreamProperties;
import kz.narxoz.carrental.client.LogNotificationClient;
import kz.narxoz.carrental.domain.NotificationPort;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Registers the downstream properties and injects them where the outbound port is built. */
@Configuration
@EnableConfigurationProperties(DownstreamProperties.class)
public class NotificationConfig {

    @Bean
    public NotificationPort notificationPort(DownstreamProperties downstream) {
        return new LogNotificationClient(downstream);
    }
}
