package kz.narxoz.carrental.domain;

/** Outbound port: tells the customer that the status changed. */
public interface NotificationPort {
    void statusChanged(RentalStatus newStatus);
}
