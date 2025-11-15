package models;

/**
 * Java 21 Record for test data
 * Immutable data carrier for parcel booking test scenarios
 */
public record ParcelTestData(
    String fromPostcode,
    String toPostcode,
    String weight,
    String deliveryType,
    String parcelType
) {
    // Compact constructor with validation
    public ParcelTestData {
        if (fromPostcode == null || fromPostcode.isBlank()) {
            throw new IllegalArgumentException("From postcode cannot be empty");
        }
        if (toPostcode == null || toPostcode.isBlank()) {
            throw new IllegalArgumentException("To postcode cannot be empty");
        }
    }
    
    // Factory method for common test scenarios
    public static ParcelTestData standardParcel(String from, String to, String weight) {
        return new ParcelTestData(from, to, weight, "STANDARD", "PARCEL");
    }
    
    public static ParcelTestData expressParcel(String from, String to, String weight) {
        return new ParcelTestData(from, to, weight, "EXPRESS", "PARCEL");
    }
}
