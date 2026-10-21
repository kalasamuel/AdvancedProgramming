package parcel.application;

/** Thrown when an aggregate cannot be found in a repository (BR6). */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
