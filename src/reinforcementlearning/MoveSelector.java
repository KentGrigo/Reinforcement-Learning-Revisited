package reinforcementlearning;

import java.util.List;

public abstract class MoveSelector {

    protected double rate;

    public MoveSelector(double rate) {
        this.rate = rate;
    }

    public abstract Move selectMove(List<Move> moves);

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
