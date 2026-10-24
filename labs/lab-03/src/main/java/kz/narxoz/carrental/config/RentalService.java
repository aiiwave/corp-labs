package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.NotificationPort;
import kz.narxoz.carrental.domain.RentalStatus;
import kz.narxoz.carrental.domain.Rule;
import org.springframework.stereotype.Service;

/** Status moves: rules + outbound port. No database here. */
@Service
public class RentalService {

    private final Rule rules;
    private final NotificationPort notifications;

    public RentalService(Rule rules, NotificationPort notifications) {
        this.rules = rules;
        this.notifications = notifications;
    }

    public RentalStatus move(RentalStatus from, RentalStatus to) {
        rules.check(from, to);
        notifications.statusChanged(to);
        return to;
    }
}
