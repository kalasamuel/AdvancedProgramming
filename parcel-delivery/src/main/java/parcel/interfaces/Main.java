package parcel.interfaces;

import parcel.application.*;
import parcel.domain.aggregates.Courier;
import parcel.domain.aggregates.Delivery;
import parcel.domain.events.ParcelPickedUpEvent;
import parcel.domain.exceptions.DomainException;
import parcel.domain.services.DeliveryAssignmentService;
import parcel.domain.valueobjects.DeliveryAddress;
import parcel.infrastructure.*;

/**
 * INTERFACE layer: the minimal entry point. It is also the composition root, so it is the one place
 * that creates the infrastructure objects and injects them into the application services (DI).
 */
public class Main {
    public static void main(String[] args) {
        // Infrastructure (concrete implementations)
        InMemoryDeliveryRepository deliveries = new InMemoryDeliveryRepository();
        InMemoryCourierRepository couriers = new InMemoryCourierRepository();
        InMemoryEventDispatcher dispatcher = new InMemoryEventDispatcher();

        // Application services receive abstractions from outside
        dispatcher.register(ParcelPickedUpEvent.class, new CourierWorkloadHandler(couriers)::handle);
        AssignDeliveryService assignService =
                new AssignDeliveryService(deliveries, couriers, new DeliveryAssignmentService());
        PickupDeliveryService pickupService = new PickupDeliveryService(deliveries, dispatcher);

        // Seed data
        couriers.save(new Courier("C-1", "Ben", "Ntinda"));
        deliveries.save(new Delivery("DEL-123",
                new DeliveryAddress("Plot 4", "Ntinda", "Kampala"),
                new DeliveryAddress("Plot 9", "Kololo", "Kampala")));

        // Use Case 1
        AssignResponseDTO a = assignService.assign(new AssignRequestDTO("DEL-123", "C-1"));
        System.out.println("UC1 assigned=" + a.assigned() + ", delivery status=" + a.status());

        // Use Case 2 (connected through the ParcelPickedUpEvent)
        PickupResponseDTO p = pickupService.pickup(new PickupRequestDTO("DEL-123"));
        System.out.println("UC2 delivery status=" + p.status());
        Courier c = couriers.findById("C-1").orElseThrow();
        System.out.println("Courier " + c.getId() + " is now " + c.getStatus()
                + " (active delivery: " + c.getActiveDeliveryId() + ")");

        try {
            pickupService.pickup(new PickupRequestDTO("FAKE-ID"));
        } catch (NotFoundException e) {
            System.out.println("BR6: " + e.getMessage());
        }
    }
}
