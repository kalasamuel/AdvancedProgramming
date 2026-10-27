package parcel.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import parcel.domain.aggregates.Courier;
import parcel.domain.repositories.ICourierRepository;

public class InMemoryCourierRepository implements ICourierRepository {
    private final Map<String, Courier> store = new HashMap<>();

    @Override
    public Optional<Courier> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Courier courier) {
        store.put(courier.getId(), courier);
    }
}
