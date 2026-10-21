package parcel.domain;

import java.util.Objects;

/**
 * AGGREGATE A - ROOT (slides 4, 5). An Entity: identified by its deliveryId.
 * Contains two DeliveryAddress value objects (its child objects).
 *
 * BR2 (state rule): the status only changes through assign() and markPickedUp(), in the order
 *                   CREATED -> ASSIGNED -> PICKED_UP. There are no public setters.
 * BR3 (invariant):  the pickup and destination addresses are never identical. It is checked in the
 *                   constructor, so an invalid Delivery can never exist.
 *
 * No Factory class on purpose (slide 6): the plain constructor is enough.
 */
public class Delivery extends Entity {

    private final DeliveryAddress pickupAddress;
    private final DeliveryAddress destinationAddress;
    private DeliveryStatus status = DeliveryStatus.CREATED;
    private String courierId;

    public Delivery(String id, DeliveryAddress pickupAddress, DeliveryAddress destinationAddress) {
        super(id);
        this.pickupAddress = Objects.requireNonNull(pickupAddress, "pickupAddress");
        this.destinationAddress = Objects.requireNonNull(destinationAddress, "destinationAddress");
        // BR3 (invariant across the two child value objects): pickup and destination are never identical
        if (pickupAddress.equals(destinationAddress)) {
            throw new DomainException("Pickup and destination addresses must be different");
        }
    }

    /** BR2 (state rule, part 1): CREATED -> ASSIGNED only when a valid courier id is provided. */
    public void assign(String courierId) {
        if (status != DeliveryStatus.CREATED) {
            throw new DomainException("Only a CREATED delivery can be assigned (current: " + status + ")");
        }
        if (courierId == null || courierId.isBlank()) {
            throw new DomainException("A valid courier id is required to assign a delivery");
        }
        this.courierId = courierId;
        this.status = DeliveryStatus.ASSIGNED;
    }

    /** BR2 (state rule, part 2): ASSIGNED -> PICKED_UP only. Also raises the BR5 event. */
    public void markPickedUp() {
        if (status != DeliveryStatus.ASSIGNED) {
            throw new DomainException("Cannot pick up a delivery that is " + status + "; it must be ASSIGNED");
        }
        this.status = DeliveryStatus.PICKED_UP;
        addEvent(new ParcelPickedUpEvent(getId(), courierId));
    }

    public DeliveryAddress getPickupAddress() { return pickupAddress; }
    public DeliveryAddress getDestinationAddress() { return destinationAddress; }
    public DeliveryStatus getStatus() { return status; }
    public String getCourierId() { return courierId; }
}
