package game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Application Entry Point.
 * Configures the main window and launches the Number Guess Game.
 */
public class Main {
    public static void main(String[] args) {
        // Set cross-platform look and feel to ensure clean baseline for custom painting
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set LookAndFeel. Proceeding with default.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Number Guess Game - CodeSoft Java Internship");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(900, 700));
            frame.setPreferredSize(new Dimension(1000, 750));

            NumberGuessGame gamePanel = new NumberGuessGame();
            frame.setContentPane(gamePanel);

            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}