package kz.narxoz.carrental.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import kz.narxoz.carrental.domain.CompositeRule;
import kz.narxoz.carrental.domain.NotificationPort;
import kz.narxoz.carrental.domain.RentalStatus;
import kz.narxoz.carrental.domain.TerminalStatusRule;
import kz.narxoz.carrental.domain.TransitionRule;
import org.junit.jupiter.api.Test;

class RentalServiceTest {

    private final NotificationPort port = mock(NotificationPort.class);
    private final RentalService service = new RentalService(
            new CompositeRule(List.of(new TerminalStatusRule(), new TransitionRule())), port);

    @Test
    void allowedMoveNotifiesCustomer() {
        assertEquals(RentalStatus.APPROVED,
                service.move(RentalStatus.REQUESTED, RentalStatus.APPROVED));
        verify(port).statusChanged(RentalStatus.APPROVED);
    }

    @Test
    void forbiddenMoveDoesNotNotify() {
        assertThrows(IllegalStateException.class,
                () -> service.move(RentalStatus.REQUESTED, RentalStatus.RENTED));
        verify(port, never()).statusChanged(RentalStatus.RENTED);
    }
}
