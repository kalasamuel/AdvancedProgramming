package parcel.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import parcel.application.EventDispatcher;
import parcel.domain.DomainEvent;

/** Simple in-process dispatcher: handlers register for an event type and get called on dispatch. */
public class InMemoryEventDispatcher implements EventDispatcher {

    private final Map<Class<? extends DomainEvent>, List<Consumer<DomainEvent>>> handlers = new HashMap<>();

    public <E extends DomainEvent> void register(Class<E> type, Consumer<E> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>())
                .add(event -> handler.accept(type.cast(event)));
    }

    @Override
    public void dispatch(DomainEvent event) {
        for (Consumer<DomainEvent> handler : handlers.getOrDefault(event.getClass(), List.of())) {
            handler.accept(event);
        }
    }
}
