package reinforcementlearning;

import java.util.Comparator;

public class Move {

    private int row;
    private int column;
    private double value;

    public Move(int row, int column, double value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public Move(int row, int column) {
        this(row, column, Double.NEGATIVE_INFINITY);
    }

    public static Comparator<Move> getIncreasingComparator() {
        return (Move o1, Move o2) -> {
            if (o1.value < o2.value) {
                return -1;
            } else if (o1.value > o2.value) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    public static Comparator<Move> getDecreasingComparator() {
        return (Move o1, Move o2) -> {
            if (o1.value > o2.value) {
                return -1;
            } else if (o1.value < o2.value) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    public static Move max(Move m1, Move m2) {
        if (m1 == null) return m2;
        if (m2 == null) return m1;
        if (m1.getValue() > m2.getValue()) {
            return m1;
        }
        return m2;
    }

    public static Move min(Move m1, Move m2) {
        if (m1 == null) return m2;
        if (m2 == null) return m1;
        if (m1.getValue() < m2.getValue()) {
            return m1;
        }
        return m2;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "Move{" + "row=" + (row + 1) + ", column=" + (column + 1) + ", value=" + value + '}';
    }

    public int getColumn() {
        return column;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
