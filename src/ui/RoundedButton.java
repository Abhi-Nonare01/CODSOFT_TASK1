package ui;

import utils.ThemeManager;
import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A modern button with rounded corners, customizable colors, and smooth hover animations.
 */
public class RoundedButton extends JButton {
    private final int cornerRadius = 20;
    private Color defaultColor;
    private Color hoverColor;
    private Color currentColor;
    private Timer animationTimer;
    private float alpha = 1.0f;
    private boolean isHovered = false;

    public RoundedButton(String text) {
        this(text, ThemeManager.getInstance().getAccentColor());
    }

    public RoundedButton(String text, Color baseColor) {
        super(text);
        this.defaultColor = baseColor;
        this.hoverColor = defaultColor.brighter();
        this.currentColor = defaultColor;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(ThemeManager.getInstance().getBoldFont());
        setForeground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                startAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                startAnimation();
            }
        });
    }

    public void setBaseColor(Color color) {
        this.defaultColor = color;
        this.hoverColor = color.brighter();
        this.currentColor = color;
        repaint();
    }

    private void startAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationTimer = new Timer(15, e -> {
            if (isHovered && alpha < 1.0f) {
                alpha += 0.1f;
            } else if (!isHovered && alpha > 0.0f) {
                alpha -= 0.1f;
            } else {
                ((Timer) e.getSource()).stop();
            }
            alpha = Math.max(0.0f, Math.min(1.0f, alpha));

            // Interpolate color
            int r = (int) (defaultColor.getRed() * (1 - alpha) + hoverColor.getRed() * alpha);
            int g = (int) (defaultColor.getGreen() * (1 - alpha) + hoverColor.getGreen() * alpha);
            int b = (int) (defaultColor.getBlue() * (1 - alpha) + hoverColor.getBlue() * alpha);
            currentColor = new Color(r, g, b);

            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        // Draw button
        g2d.setColor(currentColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight() - 2, cornerRadius, cornerRadius);

        g2d.dispose();
        super.paintComponent(g);
    }
}