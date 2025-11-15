package models;

import java.util.Map;

/**
 * Java 21 Record for environment-specific configuration
 * Provides immutable configuration per environment
 */
public record EnvironmentConfig(
    Environment environment,
    String baseUrl,
    Map<String, String> testData,
    int defaultTimeout
) {
    // Compact constructor with validation
    public EnvironmentConfig {
        if (environment == null) {
            throw new IllegalArgumentException("Environment cannot be null");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL cannot be empty");
        }
        // Make testData immutable
        testData = testData != null ? Map.copyOf(testData) : Map.of();
    }
    
    /**
     * Factory method using Java 21 enhanced switch
     * Creates environment-specific configuration with appropriate settings
     */
    public static EnvironmentConfig forEnvironment(Environment env) {
        return switch (env) {
            case PROD -> new EnvironmentConfig(
                env, 
                env.getBaseUrl(),
                Map.of(
                    "defaultPostcode", "SW1A 1AA",
                    "dataSource", "production"
                ),
                30
            );
            case TEST -> new EnvironmentConfig(
                env,
                env.getBaseUrl(),
                Map.of(
                    "defaultPostcode", "TE5T 1NG",
                    "dataSource", "test",
                    "debugMode", "true"
                ),
                60
            );
            case DEV -> new EnvironmentConfig(
                env,
                env.getBaseUrl(),
                Map.of(
                    "defaultPostcode", "DE1V 0PM",
                    "dataSource", "development",
                    "debugMode", "true",
                    "verboseLogging", "true"
                ),
                120
            );
        };
    }
    
    public String getTestDataValue(String key) {
        return testData.getOrDefault(key, "");
    }
}
