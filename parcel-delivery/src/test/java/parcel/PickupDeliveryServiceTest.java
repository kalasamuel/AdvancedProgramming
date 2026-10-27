package parcel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.application.*;
import parcel.domain.aggregates.Courier;
import parcel.domain.aggregates.Delivery;
import parcel.domain.enumerators.CourierStatus;
import parcel.domain.enumerators.DeliveryStatus;
import parcel.domain.events.ParcelPickedUpEvent;
import parcel.domain.services.DeliveryAssignmentService;
import parcel.domain.valueobjects.DeliveryAddress;
import parcel.infrastructure.*;

/** Drives both use cases from the outside, the way the Interface layer would. */
class PickupDeliveryServiceTest {

    private InMemoryDeliveryRepository deliveries;
    private InMemoryCourierRepository couriers;
    private AssignDeliveryService assignService;
    private PickupDeliveryService pickupService;

    @BeforeEach
    void wireEverything() {
        deliveries = new InMemoryDeliveryRepository();
        couriers = new InMemoryCourierRepository();
        InMemoryEventDispatcher dispatcher = new InMemoryEventDispatcher();
        dispatcher.register(ParcelPickedUpEvent.class, new CourierWorkloadHandler(couriers)::handle);
        // Dependency injection: abstractions are supplied from outside the services
        assignService = new AssignDeliveryService(deliveries, couriers, new DeliveryAssignmentService());
        pickupService = new PickupDeliveryService(deliveries, dispatcher);
    }

    @Test
    @DisplayName("T6 - BR6 (rejection): pickup with a fake DeliveryId throws NotFoundException")
    void T6_BR6_unknownDeliveryThrowsNotFound() {
        assertThrows(NotFoundException.class, () -> pickupService.pickup(new PickupRequestDTO("FAKE-ID")));
    }

    @Test
    @DisplayName("T7 - Main use case: assign (UC1) then pick up (UC2); event handled, delivery PICKED_UP, courier BUSY")
    void T7_mainUseCaseSuccess() {
        couriers.save(new Courier("C-1", "Ben", "Ntinda"));
        deliveries.save(new Delivery("DEL-123",
                new DeliveryAddress("Plot 4", "Ntinda", "Kampala"),
                new DeliveryAddress("Plot 9", "Kololo", "Kampala")));

        AssignResponseDTO assigned = assignService.assign(new AssignRequestDTO("DEL-123", "C-1"));
        assertTrue(assigned.assigned());

        PickupResponseDTO response = pickupService.pickup(new PickupRequestDTO("DEL-123"));

        assertEquals("PICKED_UP", response.status());
        assertEquals("C-1", response.courierId());
        assertEquals(DeliveryStatus.PICKED_UP, deliveries.findById("DEL-123").orElseThrow().getStatus());
        Courier courier = couriers.findById("C-1").orElseThrow();
        assertEquals(CourierStatus.BUSY, courier.getStatus());          // Aggregate B changed via the event
        assertEquals("DEL-123", courier.getActiveDeliveryId());
        assertTrue(deliveries.findById("DEL-123").orElseThrow().getEvents().isEmpty()); // event consumed once
    }
}
