package kz.narxoz.carrental.client;

import kz.narxoz.carrental.domain.NotificationPort;
import kz.narxoz.carrental.domain.RentalStatus;
import org.springframework.stereotype.Component;

/** Real adapter. It only prints; no HTTP in this lab. */
@Component
public class LogNotificationClient implements NotificationPort {

    @Override
    public void statusChanged(RentalStatus newStatus) {
        System.out.println("Notify customer: status is now " + newStatus);
    }
}
