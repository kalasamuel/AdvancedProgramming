package parcel.application;

import parcel.domain.aggregates.Courier;
import parcel.domain.events.ParcelPickedUpEvent;
import parcel.domain.repositories.ICourierRepository;

/** BR5 follow-up: when a parcel is picked up, update Aggregate B (Courier). */
public class CourierWorkloadHandler {

    private final ICourierRepository couriers;

    public CourierWorkloadHandler(ICourierRepository couriers) {
        this.couriers = couriers;
    }

    public void handle(ParcelPickedUpEvent event) {
        Courier courier = couriers.findById(event.courierId())
                .orElseThrow(() -> new NotFoundException("Courier not found: " + event.courierId()));
        courier.recordActiveDelivery(event.deliveryId());
        couriers.save(courier);
    }
}
