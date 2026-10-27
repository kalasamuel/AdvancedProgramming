package parcel.domain.repositories;

import parcel.domain.aggregates.Courier;

import java.util.Optional;

public interface ICourierRepository {
    Optional<Courier> findById(String id);
    void save(Courier courier);
}
