package PuzzleProg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Puzzle extends JPanel {
    // size of the puzzle
    private int size;
    // number of tiles in puzzle
    private int numTiles;
    //Foreground Color
    private static final Color foreGround = new Color(0, 200, 255);
    //Random to Shuffle our Pieces
    private static final Random rand = new Random();
    //Array to store our 1 dimensional objects
    private int[] tiles;
    //size of pieces
    private int tileSize;
    //blank tile
    private int blankPos;
    //margin size
    private int margin;
    //Grid size
    private int gridSize;
    private boolean gameOver;

    //Log4J setup
    private static final Logger log = LogManager.getLogger(Puzzle.class);

    private Puzzle() {
        this.size = 4;

        // Dimensions of UI
        int dimensions = 550;
        margin = 30;

        // init tiles
        numTiles = size * size - 1; //left space for blank tile
        tiles = new int[size * size];

        //grid size
        gridSize = (dimensions - 2 * margin);
        tileSize = gridSize / size;

        setPreferredSize(new Dimension(dimensions, dimensions + margin));
        setBackground(Color.WHITE);
        setForeground(foreGround);
        setFont(new Font("SansSerif", Font.BOLD, 50));

        gameOver = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                log.info("Mouse has been pressed");

                // if (gameOver) { TODO this is the correct line
                if (!gameOver) {
                    log.error("New game starting");
                    newGame();
                } else {
                    int ex = e.getX() - margin;
                    int ey = e.getY() - margin;

                    // grid clicking
                    if (ex < 0 || ex > gridSize || ey < 0 || ey > gridSize) {
                        return;
                    }

                    int c1 = ex / tileSize;
                    int r1 = ey / tileSize;

                    // position of blank piece
                    int c2 = blankPos % size;
                    int r2 = blankPos / size;

                    // convert to coordinate
                    int clickPos = r1 * size + c1;

                    int dir = 0;

                    // search direction for movement
                    if (c1 == c2 && Math.abs(r1 - r2) > 0) {
                        dir = (r1 - r2) > 0 ? size : -size;
                        log.debug("Direction is " + dir);
                    } else if (r1 == r2 && Math.abs(c1 - c2) > 0) {
                        dir = (c1 - c2) > 0 ? 1 : -1;
                        log.debug("Direction is " + dir);
                    }

                    if (dir != 0) {
                        // we move tiles in the direction
                        do {
                            // TODO this log helps us see that our loop is broken
                            log.error("moving tile");

                            int newBlankPos = blankPos + dir;
                            tiles[blankPos] = tiles[newBlankPos];
                            // blankPos = newBlankPos;
                        } while (blankPos != clickPos);
                        tiles[blankPos] = 0;
                    }

                    // we check if game is solved
                    gameOver = isSolved();
                    if (gameOver)
                        log.info("The puzzle has been solved!");
                }
                repaint();
            }
        });
        newGame();

    }

    private void newGame() {
        do {
            log.info("new game starting");
            reset();
            shuffle();
        } while (!isSolvable());

        gameOver = false;
    }

    private void reset() {
        log.debug("resetting");
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }
        // set blank tile
        blankPos = tiles.length - 1;
    }

    private void shuffle() {
        int num = numTiles;

        while (num > 1) {
            int randomInt = rand.nextInt(num--);
            int temp = tiles[randomInt];
            tiles[randomInt] = tiles[num];
            tiles[num] = temp;
        }
    }

    private boolean isSolvable() {
        int count = 0;

        for (int i = 0; i < numTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    count++;
            }
        }
        return count % 2 == 0;
    }

    private boolean isSolved() {
        if (tiles[tiles.length - 1] != 0)
            return false;

        for (int i = numTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1)
                return false;
        }
        return true;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            int row = i / size;
            int col = i % size;

            //create coordinates
            int x = margin + col * tileSize;
            int y = margin + row * tileSize;

            //special case
            if (tiles[i] == 0) {
                if (gameOver) {
                    g.setColor(foreGround);
                    drawCentString(g, "v", x, y);
                }
                continue;
            }
            //other pieces
            g.setColor(getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.WHITE);


            drawCentString(g, String.valueOf(tiles[i]), x, y);
        }
    }

    private void drawStart(Graphics2D g) {
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(foreGround);
            String s = "Click to start a new game";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }

    }

    private void drawCentString(Graphics2D g, String s, int x, int y) {
        FontMetrics fM = g.getFontMetrics();
        int ascending = fM.getAscent();
        int descending = fM.getDescent();
        g.drawString(s, x + (tileSize - fM.stringWidth(s)) / 2,
                y + (ascending + (tileSize - (ascending + descending)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2d);
        drawStart(g2d);
    }

    public static void main(String[] args) {
        log.info("Program starting");
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Puzzle");
            frame.setResizable(false);
            frame.add(new Puzzle(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
