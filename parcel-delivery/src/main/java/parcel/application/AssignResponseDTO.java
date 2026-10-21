package parcel.application;

/** Output DTO for Use Case 1. 'assigned' is false when BR4 rejects the assignment. */
public record AssignResponseDTO(String deliveryId, String courierId, boolean assigned, String status) {
}
