package models;

/**
 * Java 21 Record for window size configuration
 * Immutable data carrier for browser window dimensions
 */
public record WindowSize(int width, int height) {
    
    // Predefined window sizes
    public static final WindowSize MAXIMIZED = new WindowSize(-1, -1);
    public static final WindowSize HD_1920x1080 = new WindowSize(1920, 1080);
    public static final WindowSize HD_1366x768 = new WindowSize(1366, 768);
    public static final WindowSize TABLET_768x1024 = new WindowSize(768, 1024);
    public static final WindowSize MOBILE_375x667 = new WindowSize(375, 667);
    
    // Compact constructor with validation
    public WindowSize {
        if (width < -1 || height < -1) {
            throw new IllegalArgumentException("Width and height must be positive or -1 for maximized");
        }
    }
    
    /**
     * Factory method to parse WindowSize from string representation
     * @param sizeStr String like "MAXIMIZED", "HD_1920x1080", "1920x1080"
     * @return WindowSize object or null if not recognized
     */
    public static WindowSize fromString(String sizeStr) {
        if (sizeStr == null || sizeStr.isBlank()) {
            return MAXIMIZED;
        }
        
        // Java 21 enhanced switch with pattern matching
        return switch (sizeStr.toUpperCase()) {
            case "MAXIMIZED", "MAX" -> MAXIMIZED;
            case "HD_1920X1080", "HD", "1920X1080" -> HD_1920x1080;
            case "HD_1366X768", "1366X768" -> HD_1366x768;
            case "TABLET_768X1024", "TABLET", "768X1024" -> TABLET_768x1024;
            case "MOBILE_375X667", "MOBILE", "375X667" -> MOBILE_375x667;
            case "CUSTOM" -> null; // Caller should handle custom dimensions
            default -> {
                // Try to parse custom dimension like "1280x720"
                if (sizeStr.contains("x") || sizeStr.contains("X")) {
                    try {
                        String[] parts = sizeStr.split("[xX]");
                        if (parts.length == 2) {
                            int w = Integer.parseInt(parts[0].trim());
                            int h = Integer.parseInt(parts[1].trim());
                            yield new WindowSize(w, h);
                        }
                    } catch (NumberFormatException e) {
                        // Fall through to null
                    }
                }
                yield null;
            }
        };
    }
    
    @Override
    public String toString() {
        if (this.equals(MAXIMIZED)) {
            return "MAXIMIZED";
        }
        return width + "x" + height;
    }
}
