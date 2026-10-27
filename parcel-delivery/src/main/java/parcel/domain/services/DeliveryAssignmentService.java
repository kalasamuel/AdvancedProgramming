package parcel.domain.services;

import parcel.domain.aggregates.Courier;
import parcel.domain.aggregates.Delivery;
import parcel.domain.enumerators.CourierStatus;

/**
 * DOMAIN SERVICE (slide 6) for BR4. The rule needs data from BOTH Delivery and Courier,
 * so it belongs to neither of them. Stateless.
 *
 * Assumption: "service area matches" = the courier's service area equals the pickup area.
 */
public class DeliveryAssignmentService {

    public boolean canAssign(Delivery delivery, Courier courier) {
        return courier.getStatus() == CourierStatus.AVAILABLE
                && courier.getServiceArea().equalsIgnoreCase(delivery.getPickupAddress().area());
    }

    /** Returns false (and changes nothing) if BR4 is not satisfied. */
    public boolean assign(Delivery delivery, Courier courier) {
        if (!canAssign(delivery, courier)) {
            return false;
        }
        delivery.assign(courier.getId());
        return true;
    }
}
