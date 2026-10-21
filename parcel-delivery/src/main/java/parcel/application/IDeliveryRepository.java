package parcel.application;

import parcel.domain.Delivery;

import java.util.Optional;

/** Abstraction (slide 7): the domain says WHAT to store, not HOW. */
public interface IDeliveryRepository {
    Optional<Delivery> findById(String id);
    void save(Delivery delivery);
}
