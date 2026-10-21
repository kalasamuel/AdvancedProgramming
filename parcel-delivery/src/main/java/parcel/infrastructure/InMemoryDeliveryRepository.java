package parcel.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import parcel.domain.Delivery;
import parcel.application.IDeliveryRepository;

public class InMemoryDeliveryRepository implements IDeliveryRepository {
    private final Map<String, Delivery> store = new HashMap<>();

    @Override
    public Optional<Delivery> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Delivery delivery) {
        store.put(delivery.getId(), delivery);
    }
}
