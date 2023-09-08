package game;

import reinforcementlearning.Move;

public interface Player {

    public Move makeMove(Game game);

    public void win(Game game);

    public void lose(Game game);

    public void draw(Game game);
}
