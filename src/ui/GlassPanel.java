package ui;

import utils.ThemeManager;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.Color;

/**
 * A custom JPanel implementing Glassmorphism UI styling.
 * Features a semi-transparent background, soft borders, and rounded corners.
 */
public class GlassPanel extends JPanel {
    private int cornerRadius;

    public GlassPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemeManager theme = ThemeManager.getInstance();

        // Soft Shadow Rendering
        g2d.setColor(new Color(0, 0, 0, 15));
        for (int i = 0; i < 5; i++) {
            g2d.fillRoundRect(i, i + 2, getWidth() - (i * 2), getHeight() - (i * 2), cornerRadius, cornerRadius);
        }

        // Glass Fill
        g2d.setColor(theme.getGlassColor());
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2d.fill(roundedRectangle);

        // Frosted Glass Border
        g2d.setColor(theme.getGlassBorder());
        g2d.draw(roundedRectangle);

        g2d.dispose();
        super.paintComponent(g);
    }
}