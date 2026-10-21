package parcel.domain;

/** Thrown when a business rule (invariant) is broken, e.g. BR2, BR3. */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
