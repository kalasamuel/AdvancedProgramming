package parcel.domain.events;

/** DOMAIN EVENT (BR5): raised by Delivery when a parcel is picked up. Immutable. */
public record ParcelPickedUpEvent(String deliveryId, String courierId) implements DomainEvent {
}
