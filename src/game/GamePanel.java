package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements GameListener {

    private static final Color P1_PIECE_COLOR = new Color(Color.HSBtoRGB(0f, 1f, 1f));
    private static final Color P1_PIECE_WIN_COLOR = new Color(Color.HSBtoRGB(0f, 1f, 0.7f));
    private static final Color P2_PIECE_COLOR = new Color(Color.HSBtoRGB(0.15f, 1f, 1f));
    private static final Color P2_PIECE_WIN_COLOR = new Color(Color.HSBtoRGB(0.15f, 1f, 0.7f));
    private static final int PIECE_SIZE = 75;

    private final Game game;
    private final int width, height;
    private final List<MoveListener> listeners;

    public GamePanel(Game game) {
        this.game = game;
        this.width = game.getColumns() * PIECE_SIZE;
        this.height = game.getRows() * PIECE_SIZE;
        setPreferredSize(new Dimension(width, height));

        listeners = new ArrayList<>();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int row = (height - y) / PIECE_SIZE;
                int column = x / PIECE_SIZE;
                notifyListeners(row, column);
            }
        });
        setupKeyBindings();
        game.addGameListener(this);
    }

    private void setupKeyBindings() {
        ActionMap actionMap = getActionMap();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);

        String vkP = "VK_P";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), vkP);

        actionMap.put(vkP, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                game.printBoardTikz();
            }
        });

    }

    public void addMoveListener(MoveListener l) {
        listeners.add(l);
    }

    public void removeMoveListener(MoveListener l) {
        listeners.remove(l);
    }

    public void notifyListeners(int row, int column) {
        for (MoveListener ml : listeners) {
            ml.moveMade(row, column);
        }
    }

    @Override
    public void boardChanged() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.fillRect(0, 0, width, height);
        for (int r = 0; r < game.getRows(); r++) {
            for (int c = 0; c < game.getColumns(); c++) {
                switch (game.getPiece(r, c)) {
                    case 0:
                        g2.setColor(Color.white);
                        break;
                    case 1:
                        g2.setColor(P1_PIECE_COLOR);
                        break;
                    case 2:
                        g2.setColor(P2_PIECE_COLOR);
                        break;
                    case 3:
                        g2.setColor(P1_PIECE_WIN_COLOR);
                        break;
                    case 4:
                        g2.setColor(P2_PIECE_WIN_COLOR);
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
