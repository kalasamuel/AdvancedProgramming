package parcel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.domain.aggregates.Courier;
import parcel.domain.aggregates.Delivery;
import parcel.domain.enumerators.DeliveryStatus;
import parcel.domain.services.DeliveryAssignmentService;
import parcel.domain.valueobjects.DeliveryAddress;

class DeliveryAssignmentServiceTest {

    @Test
    @DisplayName("T4 - BR4: courier in a different area (or BUSY) => returns false and delivery stays CREATED")
    void T4_BR4_mismatchedAreaReturnsFalse() {
        DeliveryAssignmentService service = new DeliveryAssignmentService();
        Delivery d = new Delivery("DEL-1",
                new DeliveryAddress("Plot 4", "Ntinda", "Kampala"),
                new DeliveryAddress("Plot 9", "Kololo", "Kampala"));

        assertFalse(service.assign(d, new Courier("C-1", "Ben", "Entebbe")));
        assertEquals(DeliveryStatus.CREATED, d.getStatus());

        Courier busy = new Courier("C-2", "Sam", "Ntinda");
        busy.recordActiveDelivery("DEL-0");
        assertFalse(service.assign(d, busy));
        assertEquals(DeliveryStatus.CREATED, d.getStatus());
    }
}
