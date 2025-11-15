package models;

/**
 * Java 21 Record for browser configuration
 * Immutable data carrier for browser settings
 */
public record BrowserConfig(
    String browserName,
    boolean isRemote,
    boolean isHeadless,
    WindowSize windowSize,
    int implicitWaitSeconds,
    int pageLoadTimeoutSeconds,
    int scriptTimeoutSeconds,
    String userAgent,
    String downloadDir
) {
    // Compact constructor with validation
    public BrowserConfig {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }
        if (implicitWaitSeconds < 0 || pageLoadTimeoutSeconds < 0 || scriptTimeoutSeconds < 0) {
            throw new IllegalArgumentException("Timeout values cannot be negative");
        }
    }
    
    // Convenience constructor with defaults
    public BrowserConfig(String browserName, boolean isHeadless, WindowSize windowSize) {
        this(browserName, false, isHeadless, windowSize, 30, 60, 30, null, null);
    }
    
    // Factory method for common configurations
    public static BrowserConfig forBrowser(String browser) {
        return new BrowserConfig(browser, false, false, WindowSize.MAXIMIZED, 30, 60, 30, null, null);
    }
    
    public static BrowserConfig forHeadlessBrowser(String browser) {
        return new BrowserConfig(browser, false, true, WindowSize.HD_1920x1080, 30, 60, 30, null, null);
    }
    
    public static BrowserConfig forHeadlessBrowser(String browser, WindowSize size) {
        return new BrowserConfig(browser, false, true, size, 30, 60, 30, null, null);
    }
}
