package ui;

import utils.ThemeManager;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;

/**
 * A custom JPanel that paints a beautiful linear gradient background
 * based on the current active theme.
 */
public class GradientPanel extends JPanel {

    public GradientPanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        ThemeManager theme = ThemeManager.getInstance();

        // Create diagonal gradient
        LinearGradientPaint paint = new LinearGradientPaint(
                0, 0, getWidth(), getHeight(),
                new float[]{0.0f, 1.0f},
                new java.awt.Color[]{theme.getGradientStart(), theme.getGradientEnd()}
        );

        g2d.setPaint(paint);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
    }
}