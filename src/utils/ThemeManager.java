package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Manages the application theme, colors, and typography.
 * Follows the Singleton pattern and provides dark/light mode support.
 */
public class ThemeManager {
    private static ThemeManager instance;
    private boolean darkMode = true;

    // Dark Mode Colors
    private final Color darkGradientStart = new Color(15, 23, 42);
    private final Color darkGradientEnd = new Color(88, 28, 135);
    private final Color darkGlassColor = new Color(255, 255, 255, 20);
    private final Color darkGlassBorder = new Color(255, 255, 255, 40);
    private final Color darkTextPrimary = new Color(248, 250, 252);
    private final Color darkTextSecondary = new Color(148, 163, 184);

    // Light Mode Colors
    private final Color lightGradientStart = new Color(224, 242, 254);
    private final Color lightGradientEnd = new Color(216, 180, 254);
    private final Color lightGlassColor = new Color(255, 255, 255, 150);
    private final Color lightGlassBorder = new Color(255, 255, 255, 200);
    private final Color lightTextPrimary = new Color(15, 23, 42);
    private final Color lightTextSecondary = new Color(71, 85, 105);

    // Shared Colors
    private final Color accentColor = new Color(99, 102, 241);
    private final Color successColor = new Color(34, 197, 94);
    private final Color errorColor = new Color(239, 68, 68);
    private final Color warningColor = new Color(234, 179, 8);

    // Fonts
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font subHeaderFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    private ThemeManager() {}

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void toggleTheme() {
        darkMode = !darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public Color getGradientStart() { return darkMode ? darkGradientStart : lightGradientStart; }
    public Color getGradientEnd() { return darkMode ? darkGradientEnd : lightGradientEnd; }
    public Color getGlassColor() { return darkMode ? darkGlassColor : lightGlassColor; }
    public Color getGlassBorder() { return darkMode ? darkGlassBorder : lightGlassBorder; }
    public Color getTextPrimary() { return darkMode ? darkTextPrimary : lightTextPrimary; }
    public Color getTextSecondary() { return darkMode ? darkTextSecondary : lightTextSecondary; }
    public Color getAccentColor() { return accentColor; }
    public Color getSuccessColor() { return successColor; }
    public Color getErrorColor() { return errorColor; }
    public Color getWarningColor() { return warningColor; }

    public Font getHeaderFont() { return headerFont; }
    public Font getSubHeaderFont() { return subHeaderFont; }
    public Font getNormalFont() { return normalFont; }
    public Font getBoldFont() { return boldFont; }
}