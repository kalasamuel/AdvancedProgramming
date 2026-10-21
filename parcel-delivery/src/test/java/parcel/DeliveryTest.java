package parcel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.domain.*;

class DeliveryTest {

    private Delivery newDelivery() {
        return new Delivery("DEL-1",
                new DeliveryAddress("Plot 4", "Ntinda", "Kampala"),
                new DeliveryAddress("Plot 9", "Kololo", "Kampala"));
    }

    @Test
    @DisplayName("T2 - BR2 (rejection): illegal state changes throw DomainException and leave the state unchanged")
    void T2_BR2_illegalStateChangesRejected() {
        // pickup while still CREATED
        Delivery created = newDelivery();
        assertThrows(DomainException.class, created::markPickedUp);
        assertEquals(DeliveryStatus.CREATED, created.getStatus());

        // assign with a blank courier id
        assertThrows(DomainException.class, () -> created.assign(" "));
        assertEquals(DeliveryStatus.CREATED, created.getStatus());

        // assign a delivery that is already PICKED_UP
        Delivery pickedUp = newDelivery();
        pickedUp.assign("C-1");
        pickedUp.markPickedUp();
        assertThrows(DomainException.class, () -> pickedUp.assign("C-2"));
        assertEquals("C-1", pickedUp.getCourierId());
    }

    @Test
    @DisplayName("T3 - BR3 (invariant, rejection + boundary): identical pickup/destination is rejected; same area but different street is accepted")
    void T3_BR3_pickupAndDestinationMustDiffer() {
        DeliveryAddress ntinda = new DeliveryAddress("Plot 4", "Ntinda", "Kampala");

        // rejection: the aggregate can never exist in an invalid form
        assertThrows(DomainException.class,
                () -> new Delivery("DEL-2", ntinda, new DeliveryAddress("Plot 4", "Ntinda", "Kampala")));

        // boundary: only the street differs -> still valid
        Delivery ok = new Delivery("DEL-3", ntinda, new DeliveryAddress("Plot 5", "Ntinda", "Kampala"));
        assertEquals(DeliveryStatus.CREATED, ok.getStatus());
    }

    @Test
    @DisplayName("T5 - BR5: markPickedUp() records a ParcelPickedUpEvent(deliveryId, courierId)")
    void T5_BR5_markPickedUpRaisesEvent() {
        Delivery d = newDelivery();
        d.assign("C-1");
        d.markPickedUp();
        assertEquals(DeliveryStatus.PICKED_UP, d.getStatus());
        assertEquals(1, d.getEvents().size());
        assertEquals(new ParcelPickedUpEvent("DEL-1", "C-1"), d.getEvents().get(0));
    }
}
