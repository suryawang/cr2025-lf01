package exercise.codesmell;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SokobanGUI extends JFrame {
    private static final int TILE_SIZE = 50;
    private static final int GRID_ROWS = 8; // Include border rows
    private static final int GRID_COLS = 8; // Include border columns

    private static final char PLAYER = '@';
    private static final char BOX = 'B';
    private static final char GOAL = 'G';
    private static final char WALL = '#';
    private static final char EMPTY = ' ';

    private String[][] levels = {
        {
            "######",
            "#    #",
            "#   G#",
            "# G  #",
            "######"
        },
        {
            "      ",
            " @    ",
            "   B  ",
            "   B  ",
            "      "
        },
        {
            "####### ",
            "#     ##",
            "#      #",
            "# #G  G#",
            "#      #",
			"########"
        },
        {
            "        ",
            " @      ",
            "  BB    ",
            "        ",
            "        ",
			"        "
        },
        {
            "  #### ",
            "###  ##",
            "#  G  #",
            "#     #",
            "# #G  #",
            "#     #",
			"#######"
        },
        {
            "       ",
            "       ",
            " @  B  ",
            "    B  ",
            "       ",
            "       ",
			"       "
        },
        {
            " ##### ",
            "##   ##",
            "#  #  #",
            "#  G  #",
            "#  G  #",
            "#  G  #",
			"#######"
        },
        {
            "       ",
            "  @    ",
            "       ",
            "  BBB  ",
            "       ",
            "       ",
			"       "
        },
        {
            "######  ",
            "#    ###",
            "#   GG #",
            "#      #",
            "#  # G #",
            "########"
        },
        {
            "        ",
            "        ",
            "        ",
            "  BBB@  ",
            "        ",
            "        "
        }
    };

    private int currentLevel = 0;
    private char[][] level, stats;
    private int playerRow;
    private int playerCol;

    private JPanel gamePanel;

    private static Map<String, String> registeredUsers = new HashMap<>();
    private String currentUser = null;

    public SokobanGUI() {
        if (!login()) {
            JOptionPane.showMessageDialog(this, "Exiting game. Login required.");
            System.exit(0);
        }

        loadSavedProgress();
        setTitle("Sokoban Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GRID_COLS * TILE_SIZE, (GRID_ROWS + 1) * TILE_SIZE); // Extra row for level indicator
        setResizable(false);

        loadLevel(currentLevel);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawLevel(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(GRID_COLS * TILE_SIZE, (GRID_ROWS + 1) * TILE_SIZE));
        add(gamePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                processMove(e.getKeyCode());
                gamePanel.repaint();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addSaveLoadMenu();
    }

    private boolean login() {
        while (true) {
            String[] options = {"Guest", "Login", "Register"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Welcome to Sokoban! Please choose an option:",
                    "Login",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                return true;
            } else if (choice == 1) {
                String username = JOptionPane.showInputDialog("Enter username:");
                if (username == null) return false;

                String password = JOptionPane.showInputDialog("Enter password:");
                if (password == null) return false;

                if (registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(null, "Login successful! Welcome, " + username + "!");
                    currentUser = username;
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Try again.");
                }
            } else if (choice == 2) {
                String username = JOptionPane.showInputDialog("Choose a username:");
                if (username == null) return false;

                String password = JOptionPane.showInputDialog("Choose a password:");
                if (password == null) return false;

                if (registeredUsers.containsKey(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.");
                } else {
                    registeredUsers.put(username, password);
                    JOptionPane.showMessageDialog(null, "Registration successful! Please log in.");
                }
            } else {
                return false;
            }
        }
    }

    private void loadLevel(int levelIndex) {
        String[] levelStrings = levels[levelIndex*2];
		String[] statsStrings = levels[levelIndex*2+1];
        level = new char[levelStrings.length][levelStrings[0].length()];
		stats = new char[statsStrings.length][statsStrings[0].length()];

        for (int row = 0; row < levelStrings.length; row++) {
            level[row] = levelStrings[row].toCharArray();
			stats[row] = statsStrings[row].toCharArray();
        }

        findPlayerPosition();
    }

    private void findPlayerPosition() {
        for (int row = 0; row < level.length; row++) {
            for (int col = 0; col < level[row].length; col++) {
                if (stats[row][col] == PLAYER) {
                    playerRow = row;
                    playerCol = col;
                    return;
                }
            }
        }
    }

    private void drawLevel(Graphics g) {
        // Draw level indicator
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GRID_COLS * TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Level: " + (currentLevel + 1), 10, 30);

        // Draw the game grid
        for (int row = 0; row < level.length; row++) {
            for (int col = 0; col < level[row].length; col++) {
                char cell = level[row][col];
				char stat = stats[row][col];
                int x = col * TILE_SIZE;
                int y = (row + 1) * TILE_SIZE; // Offset by one row for the level indicator
				if(stat == BOX) {
					g.setColor(Color.ORANGE); // Yellow box
					g.fillRoundRect(x + 10, y + 10, TILE_SIZE - 20, TILE_SIZE - 20, 15, 15);
				} else if (stat == PLAYER) {
					g.setColor(new Color(173, 216, 230)); // Light blue
					g.fillOval(x + 15, y + 15, TILE_SIZE - 30, TILE_SIZE - 30);
				}
                switch (cell) {
                    case WALL:
                        g.setColor(Color.LIGHT_GRAY); // Same as grid
                        g.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 15, 15);
						g.setColor(Color.DARK_GRAY);
						g.drawRoundRect(x, y, TILE_SIZE, TILE_SIZE, 15, 15);
                        break;
                    case GOAL:
						g.setColor(Color.GREEN); // Green rhombus
						int[] xPoints = {x + TILE_SIZE / 2, x + TILE_SIZE - 15, x + TILE_SIZE / 2, x + 15};
						int[] yPoints = {y + 15, y + TILE_SIZE / 2, y + TILE_SIZE - 15, y + TILE_SIZE / 2};
						g.fillPolygon(xPoints, yPoints, 4);
                        break;
                }
            }
        }
		
		int x = playerCol * TILE_SIZE;
		int y = (playerRow + 1) * TILE_SIZE; // Offset by one row for the level indicator

		g.setColor(new Color(173, 216, 230)); // Light blue
		g.fillOval(x + 15, y + 15, TILE_SIZE - 30, TILE_SIZE - 30);

    }
    private boolean isGameWon() {
        for (int row = 0; row < level.length; row++)
            for (int col = 0; col < level[row].length; col++)
				if(level[row][col]==GOAL && stats[row][col]!=BOX)
					return false;
        return true;
    }

    private void processMove(int keyCode) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (keyCode) {
            case KeyEvent.VK_UP: newRow--; break;
            case KeyEvent.VK_LEFT: newCol--; break;
            case KeyEvent.VK_DOWN: newRow++; break;
            case KeyEvent.VK_RIGHT: newCol++; break;
            default: return;
        }

        if (level[newRow][newCol] == WALL) return;

        if (stats[newRow][newCol] == BOX) {
            int boxNewRow = newRow + (newRow - playerRow);
            int boxNewCol = newCol + (newCol - playerCol);
            if(stats[boxNewRow][boxNewCol] == BOX) return;
            if (level[boxNewRow][boxNewCol] == EMPTY || level[boxNewRow][boxNewCol] == GOAL) {
                stats[boxNewRow][boxNewCol] = BOX;
                stats[newRow][newCol] = PLAYER;
                stats[playerRow][playerCol] = EMPTY;
                playerRow = newRow;
                playerCol = newCol;
            }
        } else if (level[newRow][newCol] == EMPTY || level[newRow][newCol] == GOAL) {
            stats[newRow][newCol] = PLAYER;
            stats[playerRow][playerCol] = EMPTY;
            playerRow = newRow;
            playerCol = newCol;
        }

        if (isGameWon()) {
            currentLevel++;
            if (currentLevel*2 < levels.length) {
                loadLevel(currentLevel);
                gamePanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Congratulations! You completed all levels!");
                System.exit(0);
            }
        }
    }


    private void addSaveLoadMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");

        saveItem.addActionListener(e -> saveProgress());
        loadItem.addActionListener(e -> loadSavedProgress());

        menu.add(saveItem);
        menu.add(loadItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void saveProgress() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Save feature is available only for registered users.");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentUser + "_progress.dat"))) {
            oos.writeInt(currentLevel);
            oos.writeObject(level);
            JOptionPane.showMessageDialog(this, "Game progress saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving progress: " + e.getMessage());
        }
    }

    private void loadSavedProgress() {
        if (currentUser == null) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentUser + "_progress.dat"))) {
            currentLevel = ois.readInt();
            loadLevel(currentLevel);
            //level = (char[][]) ois.readObject();
            findPlayerPosition();
            gamePanel.repaint();
            JOptionPane.showMessageDialog(this, "Game progress loaded successfully!");
        } catch (IOException e) {
            // No saved progress found
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SokobanGUI::new);
    }
}
