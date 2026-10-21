package parcel.application;

import parcel.domain.Courier;
import parcel.domain.Delivery;
import parcel.domain.DeliveryAssignmentService;

/**
 * USE CASE 1: Assign Delivery to a Courier.
 * Orchestration only: look both aggregates up (BR6), ask the Domain Service (BR4), save.
 * Its output (an ASSIGNED delivery) is the input state needed by Use Case 2 (Pick Up).
 */
public class AssignDeliveryService {

    private final IDeliveryRepository deliveries;
    private final ICourierRepository couriers;
    private final DeliveryAssignmentService assignmentPolicy;

    public AssignDeliveryService(IDeliveryRepository deliveries, ICourierRepository couriers,
                                 DeliveryAssignmentService assignmentPolicy) {
        this.deliveries = deliveries;
        this.couriers = couriers;
        this.assignmentPolicy = assignmentPolicy;
    }

    public AssignResponseDTO assign(AssignRequestDTO request) {
        Delivery delivery = deliveries.findById(request.deliveryId())
                .orElseThrow(() -> new NotFoundException("Delivery not found: " + request.deliveryId()));
        Courier courier = couriers.findById(request.courierId())
                .orElseThrow(() -> new NotFoundException("Courier not found: " + request.courierId()));

        boolean assigned = assignmentPolicy.assign(delivery, courier); // BR4 (+ BR2 inside Delivery)
        if (assigned) {
            deliveries.save(delivery);
        }
        return new AssignResponseDTO(delivery.getId(), courier.getId(), assigned, delivery.getStatus().name());
    }
}
