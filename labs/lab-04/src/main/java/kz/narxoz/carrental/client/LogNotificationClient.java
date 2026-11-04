package kz.narxoz.carrental.client;

import kz.narxoz.carrental.domain.NotificationPort;
import kz.narxoz.carrental.domain.RentalStatus;

/** Real adapter. It only prints; no HTTP call in this lab. Knows where the downstream lives. */
public class LogNotificationClient implements NotificationPort {

    private final DownstreamProperties downstream;

    public LogNotificationClient(DownstreamProperties downstream) {
        this.downstream = downstream;
    }

    public DownstreamProperties downstream() {
        return downstream;
    }

    @Override
    public void statusChanged(RentalStatus newStatus) {
        System.out.println("Notify customer: status is now " + newStatus);
    }
}
