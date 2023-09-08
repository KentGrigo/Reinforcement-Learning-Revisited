package game;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardVisualizer extends JPanel {

    private static final int PIECE_SIZE = 75;

    private final int[][] board;
    private final int rows, columns;
    private final int width, height;

    public BoardVisualizer(int[][] board) {
        this.board = board;
        this.rows = board.length;
        this.columns = board[0].length;
        this.width = columns * PIECE_SIZE;
        this.height = rows * PIECE_SIZE;
        setPreferredSize(new Dimension(width, height));

    }

    public static void createBoardVisualizer(int[][] board, String name, int x, int y) {
        JFrame frame = new JFrame(name);
        frame.add(new BoardVisualizer(board));
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.fillRect(0, 0, width, height);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                switch (board[r][c]) {
                    case 0:
                        g2.setColor(Color.white);
                        break;
                    case 1:
                        g2.setColor(Color.red);
                        break;
                    case 2:
                        g2.setColor(Color.blue);
                        break;
                    default:
                        g2.setColor(Color.orange);
                        break;
                }
                int x = c * PIECE_SIZE;
                int y = height - (r + 1) * PIECE_SIZE;
                g2.fillOval(x, y, PIECE_SIZE, PIECE_SIZE);
            }
        }
    }

}
