package kz.narxoz.carrental.config;

import kz.narxoz.carrental.domain.NotificationPort;
import kz.narxoz.carrental.domain.RentalNotFound;
import kz.narxoz.carrental.domain.RentalStatus;
import kz.narxoz.carrental.domain.Rule;
import kz.narxoz.carrental.persistence.RentalJdbc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Moves a stored request to a new status: read, check rules, UPDATE, notify. One transaction. */
@Service
public class RentalStatusService {

    private final RentalJdbc rentals;
    private final Rule rules;
    private final NotificationPort notifications;

    public RentalStatusService(RentalJdbc rentals, Rule rules, NotificationPort notifications) {
        this.rentals = rentals;
        this.rules = rules;
        this.notifications = notifications;
    }

    @Transactional
    public RentalStatus move(String businessKey, RentalStatus to) {
        RentalStatus from = rentals.findStatus(businessKey)
                .orElseThrow(() -> new RentalNotFound(businessKey));
        rules.check(from, to); // forbidden move -> IllegalStateException -> nothing is written
        rentals.updateStatus(businessKey, to);
        notifications.statusChanged(to);
        return to;
    }
}
