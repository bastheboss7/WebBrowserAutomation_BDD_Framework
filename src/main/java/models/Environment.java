package models;

/**
 * Java 21 Enum for environment configuration
 * Supports PROD, TEST, and DEV environments as per requirements
 */
public enum Environment {
    PROD("https://www.evri.com/", "Production"),
    TEST("https://www.test.evri.com/", "Test"),
    DEV("https://www.dev.evri.com/", "Development");
    
    private final String baseUrl;
    private final String description;
    
    Environment(String baseUrl, String description) {
        this.baseUrl = baseUrl;
        this.description = description;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Java 21 enhanced switch expression for environment selection
     * @param name Environment name (case-insensitive)
     * @return Environment enum, defaults to PROD if unknown
     */
    public static Environment fromString(String name) {
        if (name == null || name.isBlank()) {
            return PROD;
        }
        
        return switch (name.toLowerCase().trim()) {
            case "prod", "production" -> PROD;
            case "test", "testing" -> TEST;
            case "dev", "development" -> DEV;
            default -> {
                System.err.println("Unknown environment: '" + name + "', defaulting to PROD");
                yield PROD;
            }
        };
    }
    
    @Override
    public String toString() {
        return description + " (" + baseUrl + ")";
    }
}
