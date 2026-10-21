package parcel.domain;

/**
 * AGGREGATE B - ROOT (slide 5). Invariant: a BUSY courier cannot accept another active delivery.
 */
public class Courier extends Entity {

    private final String name;
    private final String serviceArea;
    private CourierStatus status = CourierStatus.AVAILABLE;
    private String activeDeliveryId;

    public Courier(String id, String name, String serviceArea) {
        super(id);
        if (serviceArea == null || serviceArea.isBlank()) {
            throw new IllegalArgumentException("Service area cannot be empty");
        }
        this.name = name;
        this.serviceArea = serviceArea;
    }

    /** Called by the event handler after a parcel is picked up (BR5). */
    public void recordActiveDelivery(String deliveryId) {
        if (status == CourierStatus.BUSY) {
            throw new DomainException("Courier " + getId() + " is already BUSY with delivery " + activeDeliveryId);
        }
        this.activeDeliveryId = deliveryId;
        this.status = CourierStatus.BUSY;
    }

    public String getName() { return name; }
    public String getServiceArea() { return serviceArea; }
    public CourierStatus getStatus() { return status; }
    public String getActiveDeliveryId() { return activeDeliveryId; }
}
