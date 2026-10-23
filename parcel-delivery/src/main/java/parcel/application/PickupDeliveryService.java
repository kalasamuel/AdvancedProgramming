package parcel.application;

import parcel.domain.aggregates.Delivery;
import parcel.domain.events.DomainEvent;
import parcel.domain.repositories.IDeliveryRepository;

/**
 * USE CASE 2: Pick Up Delivery (slides 9, 10, 13).
 * Orchestrates only: load -> call domain behaviour -> save -> dispatch events.
 * Contains NO business rules. Receives its repository through the constructor (Dependency Injection).
 */
public class PickupDeliveryService {

    private final IDeliveryRepository deliveries;
    private final EventDispatcher dispatcher;

    public PickupDeliveryService(IDeliveryRepository deliveries, EventDispatcher dispatcher) {
        this.deliveries = deliveries;
        this.dispatcher = dispatcher;
    }

    public PickupResponseDTO pickup(PickupRequestDTO request) {
        // BR6: must retrieve the existing delivery first
        Delivery delivery = deliveries.findById(request.deliveryId())
                .orElseThrow(() -> new NotFoundException("Delivery not found: " + request.deliveryId()));

        delivery.markPickedUp();      // BR3 enforced inside the aggregate
        deliveries.save(delivery);    // persist first...

        for (DomainEvent event : delivery.pullEvents()) {
            dispatcher.dispatch(event); // ...then dispatch (BR5)
        }
        return new PickupResponseDTO(delivery.getId(), delivery.getStatus().name(), delivery.getCourierId());
    }
}
