# GuessMaster - Number Guessing Game 🎮
**CodeSoft Java Internship - Task 1**

Welcome to **GuessMaster**, a highly interactive and modern desktop application built for the CodeSoft Java Internship. This is not just a basic console game; it features a premium Graphical User Interface (GUI) built entirely with Java Swing and Java2D, showcasing advanced UI/UX design.

## 🌟 Project Features
- **Dynamic Difficulty Levels:** Choose between Easy (1-50), Medium (1-100), and Hard (1-200) modes.
- **XP & Leveling System:** Gamified experience where players earn XP and level up upon winning.
- **Session Leaderboard:** Tracks and displays the top 5 high scores during the current session.
- **Live Statistics:** Monitors Total Wins, Games Played, and Time Elapsed (in seconds).
- **Glassmorphism UI Engine:** Features semi-transparent frosted glass panels, soft shadows, and clean gradients without using external libraries.
- **Dynamic Theming:** One-click toggle between a sleek Dark Mode and a crisp Light Mode.
- **Smooth Animations:** Custom hover animations and color interpolations on buttons.

## 📁 Folder Structure
```text
src
│
├── game
│      Main.java                 # Application Entry Point
│      NumberGuessGame.java      # Core Game Logic & UI Dashboard
│
├── ui
│      GradientPanel.java         # High-quality Background Renderer
│      RoundedButton.java        # Animated Custom Buttons
│      RoundedTextField.java     # Modern Input Fields
│      GlassPanel.java           # Frosted Glassmorphism Containers
│
└── utils
       ThemeManager.java         # Singleton Color & Typography State