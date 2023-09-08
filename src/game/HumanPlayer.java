package game;

import java.util.logging.Level;
import java.util.logging.Logger;
import reinforcementlearning.Move;

public class HumanPlayer extends BasePlayer implements MoveListener {

    private final GamePanel gamePanel;
    private Move potentialMove;

    public HumanPlayer(GamePanel gp) {
        this.gamePanel = gp;
        this.potentialMove = null;
        gp.addMoveListener(this);
    }

    @Override
    public Move makeMove(Game game) {
        potentialMove = null;
        while (potentialMove == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(HumanPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return potentialMove; //game will ask for new move
    }

    @Override
    public void moveMade(int row, int column) {
        potentialMove = new Move(row, column);
    }
}
