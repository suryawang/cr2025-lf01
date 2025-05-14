package answer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Tetris extends JFrame {
	
    public Tetris() {
        JLabel statusbar = new JLabel(" 0");
        Board board = new Board(statusbar);
        
        add(statusbar, BorderLayout.SOUTH);
        add(board);
        
        board.start(200);

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Tetris game = new Tetris();
            game.setVisible(true);
        });
    }
}


class Shape {
    private static final int SHAPE_POINTS = 4;
	private Tetromino pieceShape;
    private int coords[][];
    private int[][][] coordsTable;

    public Shape() {
        coords = new int[4][2];
        
        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        setShape(Tetromino.NoShape);
    }

    protected void setShape(Tetromino shape) {
        for (int i = 0; i < 4 ; i++)
            for (int j = 0; j < 2; ++j)
                coords[i][j] = coordsTable[shape.ordinal()][i][j];

        pieceShape = shape;
    }

    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetromino getShape()  { return pieceShape; }
    
    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Tetromino[] values = Tetromino.values();
        setShape(values[x]);
    }

    public int minX() {
        int m = coords[0][0];
        for (int i=0; i < SHAPE_POINTS; i++)
            m = Math.min(m, coords[i][0]);
        return m;
    }
    
    public int minY() {
        int m = coords[0][1];
        for (int i=0; i < SHAPE_POINTS; i++)
            m = Math.min(m, coords[i][1]);
        return m;
    }
    
    
    public Shape rotate(int direction) {
        if (pieceShape == Tetromino.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < SHAPE_POINTS; ++i) {
            int newX = (direction == 1) ? y(i) : -y(i);
            int newY = (direction == 1) ? -x(i) : x(i);
            result.setX(i, newX);
            result.setY(i, newY);
        }

        return result;
    }
    
//    public Shape rotateLeft() {
//        if (pieceShape == Tetromino.SquareShape)
//            return this;
//
//        Shape result = new Shape();
//        result.pieceShape = pieceShape;
//
//        for (int i = 0; i < SHAPE_POINTS; ++i) {
//            result.setX(i, y(i));
//            result.setY(i, -x(i));
//        }
//
//        return result;
//    }
//
//    public Shape rotateRight() {
//        if (pieceShape == Tetromino.SquareShape)
//            return this;
//
//        Shape result = new Shape();
//        result.pieceShape = pieceShape;
//
//        for (int i = 0; i < SHAPE_POINTS; ++i) {
//            result.setX(i, -y(i));
//            result.setY(i, x(i));
//        }
//
//        return result;
//    }
}

enum Tetromino { NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape }
	
class Board extends JPanel {
    private final int WIDTH = 10;
    private final int HEIGHT = 22;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;

    private Tetromino[] minoe;

    public Board(JLabel status) {
        setFocusable(true);
        statusbar = status;
        addKeyListener(new TAdapter());
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / HEIGHT;
    }

    private Tetromino shapeAt(int x, int y) {
        return minoe[(y * WIDTH) + x];
    }
    
    void start(int delay) {
        curPiece = new Shape();
        minoe = new Tetromino[WIDTH * HEIGHT];

        // clear board
		for (int i = 0; i < HEIGHT * WIDTH; i++)
            minoe[i] = Tetromino.NoShape;

        newPiece();

        timer = new Timer(delay, new GameCycle());
        timer.start();
    }

    private void pause() {
        isPaused = !isPaused;
        if (isPaused)
            statusbar.setText("paused");
        else statusbar.setText(String.valueOf(numLinesRemoved));

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        int boardTop = (int) getSize().getHeight() - HEIGHT * squareHeight();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                Tetromino shape = shapeAt(j, HEIGHT - i - 1);

                if (shape != Tetromino.NoShape)
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
            }
        }

        if (curPiece.getShape() != Tetromino.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawSquare(g, x * squareWidth(),
                        boardTop + (HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = curY;

        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            newY--;
        }

        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            minoe[(y * WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished)
            newPiece();
    }
    
    private void newPiece() {
        curPiece.setRandomShape();
        curX = WIDTH / 2 + 1;
        curY = HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Tetromino.NoShape);
            timer.stop();

            String msg = String.format("Game over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
        }
    }
    
    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

			// check boundary
            if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
                return false;

            if (shapeAt(x, y) != Tetromino.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {
        int numFullLines = countAndRemoveFullLines();
        
        if (numFullLines > 0) {
            updateGameState(numFullLines);
        }
    }

    private int countAndRemoveFullLines() {
        int numFullLines = 0;

        for (int i = HEIGHT - 1; i >= 0; i--) {
            if (isLineFull(i)) {
                numFullLines++;
                shiftLinesDown(i);
            }
        }

        return numFullLines;
    }

    private boolean isLineFull(int row) {
        for (int j = 0; j < WIDTH; j++) {
            if (shapeAt(j, row) == Tetromino.NoShape) {
                return false;
            }
        }
        return true;
    }

    private void shiftLinesDown(int fromRow) {
        for (int k = fromRow; k < HEIGHT - 1; k++) {
            for (int j = 0; j < WIDTH; j++) {
                minoe[(k * WIDTH) + j] = shapeAt(j, k + 1);
            }
        }
    }

    private void updateGameState(int numFullLines) {
        numLinesRemoved += numFullLines;
        statusbar.setText(String.valueOf(numLinesRemoved));
        isFallingFinished = true;
        curPiece.setShape(Tetromino.NoShape);
    }

    
//    private void removeFullLines() {
//        int numFullLines = 0;
//
//        for (int i = HEIGHT - 1; i >= 0; i--) {
//            boolean lineIsFull = true;
//
//            for (int j = 0; j < WIDTH; j++) {
//                if (shapeAt(j, i) == Tetromino.NoShape) {
//                    lineIsFull = false;
//                    break;
//                }
//            }
//
//            if (lineIsFull) {
//                numFullLines++;
//
//                for (int k = i; k < HEIGHT - 1; k++)
//                    for (int j = 0; j < WIDTH; j++)
//                        minoe[(k * WIDTH) + j] = shapeAt(j, k + 1);
//            }
//        }
//
//        if (numFullLines > 0) {
//            numLinesRemoved += numFullLines;
//
//            statusbar.setText(String.valueOf(numLinesRemoved));
//            isFallingFinished = true;
//            curPiece.setShape(Tetromino.NoShape);
//        }
//    }
    

    private void drawSquare(Graphics g, int x, int y, Tetromino shape) {
        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    private class GameCycle implements ActionListener {
       @Override
        public void actionPerformed(ActionEvent e) {
           update();
           repaint();
        }
    }

//    private void doGameCycle() {
//        update();
//        repaint();
//    }

    private void update() {
        if (isPaused) return;

        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else
            oneLineDown();
    }

    class TAdapter extends KeyAdapter {
        @Override	
        public void keyPressed(KeyEvent e) {
            if (curPiece.getShape() == Tetromino.NoShape)
                return;

            int keycode = e.getKeyCode();
            
            switch (keycode) {
                case KeyEvent.VK_P:  pause(); break;
                case KeyEvent.VK_LEFT:  tryMove(curPiece, curX - 1, curY); break;
                case KeyEvent.VK_RIGHT:  tryMove(curPiece, curX + 1, curY); break;
                case KeyEvent.VK_DOWN:  tryMove(curPiece.rotate(-1), curX, curY); break;
                case KeyEvent.VK_UP:  tryMove(curPiece.rotate(1), curX, curY); break;
                case KeyEvent.VK_SPACE:  dropDown(); break;
                case KeyEvent.VK_D:  oneLineDown(); break;
            }
        }
    }
}