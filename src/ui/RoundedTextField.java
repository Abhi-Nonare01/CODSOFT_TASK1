package ui;

import utils.ThemeManager;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A modern text field with rounded corners, padding, and glassmorphism styling.
 */
public class RoundedTextField extends JTextField {
    private final int cornerRadius = 20;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setFont(ThemeManager.getInstance().getNormalFont());
        setForeground(ThemeManager.getInstance().getTextPrimary());
        setCaretColor(ThemeManager.getInstance().getAccentColor());
        setHorizontalAlignment(JTextField.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();

        // Background
        g2d.setColor(theme.getGlassColor());
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Border
        g2d.setColor(isFocusOwner() ? theme.getAccentColor() : theme.getGlassBorder());
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2d.dispose();

        // Ensure text is painted using the current theme color
        setForeground(ThemeManager.getInstance().getTextPrimary());
        super.paintComponent(g);
    }
}