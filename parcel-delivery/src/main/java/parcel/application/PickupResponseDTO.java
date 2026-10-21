package parcel.application;

/** Boundary data going OUT: plain strings, so Domain objects never leak to the caller. */
public record PickupResponseDTO(String deliveryId, String status, String courierId) {
}
