package parcel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.application.CourierWorkloadHandler;
import parcel.domain.*;
import parcel.infrastructure.InMemoryCourierRepository;

class CourierWorkloadHandlerTest {

    @Test
    @DisplayName("T8 - Aggregate B rejects follow-up: BUSY courier throws DomainException, final state unchanged")
    void T8_busyCourierRejectsFollowUp() {
        InMemoryCourierRepository couriers = new InMemoryCourierRepository();
        Courier courier = new Courier("C-1", "Ben", "Ntinda");
        courier.recordActiveDelivery("DEL-OLD");
        couriers.save(courier);
        CourierWorkloadHandler handler = new CourierWorkloadHandler(couriers);

        DomainException outcome = assertThrows(DomainException.class,
                () -> handler.handle(new ParcelPickedUpEvent("DEL-NEW", "C-1")));

        assertTrue(outcome.getMessage().contains("BUSY"));              // the returned outcome
        assertEquals(CourierStatus.BUSY, courier.getStatus());          // final state
        assertEquals("DEL-OLD", courier.getActiveDeliveryId());         // not overwritten
    }
}
