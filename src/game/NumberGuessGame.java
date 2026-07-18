package game;

import ui.GlassPanel;
import ui.GradientPanel;
import ui.RoundedButton;
import ui.RoundedTextField;
import utils.ThemeManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Core Game Logic and Main UI Dashboard.
 * Implements game rules, validation, XP, statistics, and handles rendering.
 */
public class NumberGuessGame extends GradientPanel {

    private enum Difficulty {
        EASY(1, 50, 10, 10),
        MEDIUM(1, 100, 7, 20),
        HARD(1, 200, 5, 50);

        final int min;
        final int max;
        final int maxAttempts;
        final int xpReward;

        Difficulty(int min, int max, int maxAttempts, int xpReward) {
            this.min = min;
            this.max = max;
            this.maxAttempts = maxAttempts;
            this.xpReward = xpReward;
        }
    }

    // Game State
    private int targetNumber;
    private int attemptsLeft;
    private int currentScore;
    private int highScore = 0;
    private int totalGamesPlayed = 0;
    private int totalWins = 0;
    private int playerXP = 0;
    private int playerLevel = 1;
    private int secondsElapsed = 0;
    private boolean isGameActive = false;
    private Difficulty currentDifficulty = Difficulty.MEDIUM;
    private Timer gameTimer;
    private final List<Integer> leaderboard = new ArrayList<>();

    // UI Components
    private JLabel statusLabel;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel xpLabel;
    private JLabel levelLabel;
    private JLabel statsLabel;
    private RoundedTextField guessInput;
    private RoundedButton guessButton;
    private RoundedButton restartButton;
    private RoundedButton themeToggleButton;
    private JComboBox<String> difficultySelector;
    private JPanel leaderboardPanel;

    public NumberGuessGame() {
        super();
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initializeUI();
        setupTimer();
        startNewGame();
    }

    private void initializeUI() {
        ThemeManager theme = ThemeManager.getInstance();

        // TOP HEADER: Stats & Theme Toggle
        GlassPanel headerPanel = new GlassPanel(20);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftHeader.setOpaque(false);

        levelLabel = createStyledLabel("Level: 1", theme.getSubHeaderFont(), theme.getAccentColor());
        xpLabel = createStyledLabel("XP: 0/100", theme.getBoldFont(), theme.getTextPrimary());
        scoreLabel = createStyledLabel("High Score: 0", theme.getBoldFont(), theme.getWarningColor());

        leftHeader.add(levelLabel);
        leftHeader.add(xpLabel);
        leftHeader.add(scoreLabel);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightHeader.setOpaque(false);

        themeToggleButton = new RoundedButton("Toggle Theme", new Color(100, 116, 139));
        themeToggleButton.addActionListener(e -> toggleTheme());
        rightHeader.add(themeToggleButton);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // CENTER: Main Game Area
        GlassPanel centerPanel = new GlassPanel(30);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        statusLabel = createStyledLabel("Guess the number between X and Y", theme.getHeaderFont(), theme.getTextPrimary());
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        centerPanel.add(statusLabel, gbc);

        feedbackLabel = createStyledLabel("Waiting for your guess...", theme.getSubHeaderFont(), theme.getTextSecondary());
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        centerPanel.add(feedbackLabel, gbc);

        guessInput = new RoundedTextField(15);
        guessInput.setFont(new Font("Segoe UI", Font.BOLD, 24));
        guessInput.addActionListener(e -> processGuess());
        gbc.gridy = 2;
        centerPanel.add(guessInput, gbc);

        guessButton = new RoundedButton("Submit Guess", theme.getAccentColor());
        guessButton.setPreferredSize(new Dimension(200, 45));
        guessButton.addActionListener(e -> processGuess());
        gbc.gridy = 3;
        centerPanel.add(guessButton, gbc);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setOpaque(false);
        attemptsLabel = createStyledLabel("Attempts Left: 0", theme.getBoldFont(), theme.getErrorColor());
        timerLabel = createStyledLabel("Time: 00:00", theme.getBoldFont(), theme.getTextPrimary());
        infoPanel.add(attemptsLabel);
        infoPanel.add(timerLabel);
        gbc.gridy = 4;
        centerPanel.add(infoPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // EAST: Side Panel (Controls & Leaderboard)
        GlassPanel sidePanel = new GlassPanel(20);
        sidePanel.setPreferredSize(new Dimension(280, 0));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel settingsTitle = createStyledLabel("Game Settings", theme.getSubHeaderFont(), theme.getTextPrimary());
        settingsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(settingsTitle);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] diffOptions = {"Easy (1-50)", "Medium (1-100)", "Hard (1-200)"};
        difficultySelector = new JComboBox<>(diffOptions);
        difficultySelector.setSelectedIndex(1);
        difficultySelector.setMaximumSize(new Dimension(200, 30));
        difficultySelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultySelector.addActionListener(e -> changeDifficulty());
        sidePanel.add(difficultySelector);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        restartButton = new RoundedButton("Restart Game", theme.getWarningColor());
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setMaximumSize(new Dimension(200, 40));
        restartButton.addActionListener(e -> startNewGame());
        sidePanel.add(restartButton);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        statsLabel = createStyledLabel("Wins: 0 | Played: 0", theme.getNormalFont(), theme.getTextSecondary());
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(statsLabel);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel leaderTitle = createStyledLabel("Session Top Scores", theme.getSubHeaderFont(), theme.getTextPrimary());
        leaderTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(leaderTitle);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setOpaque(false);
        sidePanel.add(leaderboardPanel);

        add(sidePanel, BorderLayout.EAST);
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private void setupTimer() {
        gameTimer = new Timer(1000, e -> {
            if (isGameActive) {
                secondsElapsed++;
                updateTimerDisplay();
            }
        });
    }

    private void updateTimerDisplay() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void changeDifficulty() {
        int selected = difficultySelector.getSelectedIndex();
        if (selected == 0) currentDifficulty = Difficulty.EASY;
        else if (selected == 1) currentDifficulty = Difficulty.MEDIUM;
        else currentDifficulty = Difficulty.HARD;

        if (!isGameActive || attemptsLeft == currentDifficulty.maxAttempts) {
            startNewGame();
        }
    }

    private void startNewGame() {
        targetNumber = new Random().nextInt(currentDifficulty.max - currentDifficulty.min + 1) + currentDifficulty.min;
        attemptsLeft = currentDifficulty.maxAttempts;
        currentScore = 1000; // Base score, decreases with time and attempts
        secondsElapsed = 0;
        isGameActive = true;

        statusLabel.setText("Guess between " + currentDifficulty.min + " and " + currentDifficulty.max);
        feedbackLabel.setText("Good luck!");
        feedbackLabel.setForeground(ThemeManager.getInstance().getTextSecondary());
        guessInput.setText("");
        guessInput.setEditable(true);
        guessButton.setEnabled(true);

        updateDisplays();
        updateTimerDisplay();
        gameTimer.start();
        playSound("start.wav");
    }

    private void processGuess() {
        if (!isGameActive) return;

        String input = guessInput.getText().trim();
        if (input.isEmpty()) return;

        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            showFeedback("Invalid input! Enter a number.", ThemeManager.getInstance().getErrorColor());
            guessInput.setText("");
            playSound("error.wav");
            return;
        }

        if (guess < currentDifficulty.min || guess > currentDifficulty.max) {
            showFeedback("Out of bounds! (" + currentDifficulty.min + " - " + currentDifficulty.max + ")", ThemeManager.getInstance().getWarningColor());
            guessInput.setText("");
            return;
        }

        attemptsLeft--;
        currentScore -= 50; // Penalty per attempt

        if (guess == targetNumber) {
            handleWin();
        } else if (attemptsLeft <= 0) {
            handleLoss();
        } else {
            String hint = guess < targetNumber ? "Too Low!" : "Too High!";
            showFeedback(hint, ThemeManager.getInstance().getWarningColor());
            playSound("wrong.wav");
        }

        guessInput.setText("");
        updateDisplays();
    }

    private void handleWin() {
        isGameActive = false;
        gameTimer.stop();
        totalGamesPlayed++;
        totalWins++;

        // Calculate final score
        currentScore -= secondsElapsed * 2; // Time penalty
        currentScore = Math.max(10, currentScore);

        if (currentScore > highScore) {
            highScore = currentScore;
        }

        addXP(currentDifficulty.xpReward + (attemptsLeft * 5));
        updateLeaderboard(currentScore);

        showFeedback("Correct! You won in " + secondsElapsed + "s! Score: " + currentScore, ThemeManager.getInstance().getSuccessColor());
        guessInput.setEditable(false);
        guessButton.setEnabled(false);
        playSound("win.wav");
    }

    private void handleLoss() {
        isGameActive = false;
        gameTimer.stop();
        totalGamesPlayed++;

        showFeedback("Game Over! The number was " + targetNumber, ThemeManager.getInstance().getErrorColor());
        guessInput.setEditable(false);
        guessButton.setEnabled(false);
        playSound("lose.wav");
    }

    private void showFeedback(String message, Color color) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(color);
    }

    private void addXP(int amount) {
        playerXP += amount;
        int xpNeeded = playerLevel * 100;
        if (playerXP >= xpNeeded) {
            playerLevel++;
            playerXP -= xpNeeded;
            playSound("levelup.wav");
        }
    }

    private void updateDisplays() {
        attemptsLabel.setText("Attempts Left: " + attemptsLeft);
        scoreLabel.setText("High Score: " + highScore);
        levelLabel.setText("Level: " + playerLevel);
        xpLabel.setText("XP: " + playerXP + "/" + (playerLevel * 100));
        statsLabel.setText("Wins: " + totalWins + " | Played: " + totalGamesPlayed);
    }

    private void updateLeaderboard(int score) {
        leaderboard.add(score);
        Collections.sort(leaderboard, Collections.reverseOrder());
        if (leaderboard.size() > 5) {
            leaderboard.remove(5);
        }

        leaderboardPanel.removeAll();
        ThemeManager theme = ThemeManager.getInstance();
        int rank = 1;
        for (int s : leaderboard) {
            JLabel entry = createStyledLabel(rank + ". " + s + " pts", theme.getBoldFont(), theme.getTextSecondary());
            entry.setAlignmentX(Component.CENTER_ALIGNMENT);
            leaderboardPanel.add(entry);
            leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            rank++;
        }
        leaderboardPanel.revalidate();
        leaderboardPanel.repaint();
    }

    private void toggleTheme() {
        ThemeManager.getInstance().toggleTheme();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        // Force update our custom colored labels
        ThemeManager theme = ThemeManager.getInstance();
        levelLabel.setForeground(theme.getAccentColor());
        xpLabel.setForeground(theme.getTextPrimary());
        scoreLabel.setForeground(theme.getWarningColor());
        statusLabel.setForeground(theme.getTextPrimary());
        timerLabel.setForeground(theme.getTextPrimary());
        statsLabel.setForeground(theme.getTextSecondary());

        for (Component c : leaderboardPanel.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(theme.getTextSecondary());
            }
        }
        repaint();
    }

    private void playSound(String fileName) {
        try {
            File soundFile = new File("assets/sounds/" + fileName);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } else {
                // Silently skip if asset directory or file is missing
                // to maintain "Production Ready" requirement without breaking
                System.out.println("Audio file not found: " + soundFile.getPath());
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}