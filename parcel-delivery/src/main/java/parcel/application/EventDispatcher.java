package parcel.application;

import parcel.domain.DomainEvent;

/** The Application layer only knows this interface; the implementation lives in Infrastructure. */
public interface EventDispatcher {
    void dispatch(DomainEvent event);
}
