package parcel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parcel.domain.valueobjects.DeliveryAddress;

class DeliveryAddressTest {

    @Test
    @DisplayName("T1 - BR1 (boundary + rejection): empty/blank area or city is rejected, 1-character area is accepted")
    void T1_BR1_emptyAreaRejectsCreation() {
        // rejection cases
        assertThrows(IllegalArgumentException.class, () -> new DeliveryAddress("Street", "", "City"));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryAddress("Street", "   ", "City"));
        assertThrows(IllegalArgumentException.class, () -> new DeliveryAddress("Street", "Area", ""));
        // boundary: the shortest valid area
        assertEquals("A", new DeliveryAddress("Street", "A", "City").area());
    }
}
