package parcel.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Layer Supertype (slide 14): deliberately minimal.
 * Holds only an id and the list of domain events the aggregate has raised.
 */
public abstract class Entity {

    private final String id;
    private final List<DomainEvent> events = new ArrayList<>();

    protected Entity(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be empty");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    protected void addEvent(DomainEvent event) {
        events.add(event);
    }

    /** Read-only view of the events raised so far (used by tests, e.g. T5). */
    public List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /** Returns the events and clears the internal list, so each event is dispatched only once. */
    public List<DomainEvent> pullEvents() {
        List<DomainEvent> copy = new ArrayList<>(events);
        events.clear();
        return copy;
    }
}
