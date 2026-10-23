package parcel.domain.valueobjects;

/**
 * VALUE OBJECT (slide 4). No identity: two addresses with the same values are equal.
 * A Java 'record' gives us equals(), hashCode(), toString() and immutability for free.
 *
 * BR1: area and city must not be empty. Violation -> IllegalArgumentException
 * (the Java equivalent of Python's ValueError).
 */
public record DeliveryAddress(String street, String area, String city) {

    public DeliveryAddress {
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException("Area cannot be empty");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        street = (street == null) ? "" : street;
    }
}
