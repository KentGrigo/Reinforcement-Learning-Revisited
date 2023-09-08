package reinforcementlearning;

import auxiliaries.PrintUtil;
import auxiliaries.RNG;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

public class SoftmaxMoveSelector extends MoveSelector {

    private final MoveSelector greedy;

    public SoftmaxMoveSelector(double temperature) {
        super(temperature);
        greedy = new EpsilonGreedyMoveSelector(0);
    }

    public Move selectMoveUnstable(List<Move> moves) {
        if (rate == 0) {
            return greedy.selectMove(moves);
        }
        double[] probabilities = new double[moves.size()];
        probabilities[0] = 1 + FastMath.expm1(moves.get(0).getValue() / rate);
        for (int i = 1; i < moves.size(); i++) {
            probabilities[i] = 1 + FastMath.expm1(moves.get(i).getValue() / rate) + probabilities[i - 1];
        }
        System.out.println(PrintUtil.formatArray(probabilities));
        double random = RNG.nextDouble() * probabilities[moves.size() - 1];
        for (int i = 0; i < moves.size(); i++) {
            if (random < probabilities[i]) return moves.get(i);
        }

        return moves.get(moves.size() - 1);
    }

    @Override
    public Move selectMove(List<Move> moves) {
        if (rate == 0) {
            return greedy.selectMove(moves);
        }
        double largestMoveValue = 0;
        for (int i = 0; i < moves.size(); i++) {
            largestMoveValue = Math.max(largestMoveValue, moves.get(i).getValue() / rate);
        }
        double sum = 0;
        for (int i = 0; i < moves.size(); i++) {
            sum += 1 + FastMath.expm1(moves.get(i).getValue() / rate - largestMoveValue);
        }
        double denominator = FastMath.log(sum) + largestMoveValue;

        double[] probabilities = new double[moves.size()];
        probabilities[0] = 1 + FastMath.expm1(moves.get(0).getValue() / rate - denominator);
        for (int i = 1; i < moves.size(); i++) {
            probabilities[i] = 1 + FastMath.expm1(moves.get(i).getValue() / rate - denominator) + probabilities[i - 1];
        }

        double random = RNG.nextDouble();
        for (int i = 0; i < moves.size(); i++) {
            if (random < probabilities[i]) return moves.get(i);
        }

        return moves.get(moves.size() - 1);
    }

}
