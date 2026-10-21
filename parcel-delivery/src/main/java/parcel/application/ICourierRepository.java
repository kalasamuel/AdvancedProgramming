package parcel.application;

import parcel.domain.Courier;

import java.util.Optional;

public interface ICourierRepository {
    Optional<Courier> findById(String id);
    void save(Courier courier);
}
