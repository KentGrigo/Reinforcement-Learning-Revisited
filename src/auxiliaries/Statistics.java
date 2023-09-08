package auxiliaries;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Statistics {

    private ArrayList<Integer> wins, loss, draws, amountOfGames;
    private int epochs;
    private DecimalFormat df;

    public Statistics() {
        this.wins = new ArrayList<>();
        this.loss = new ArrayList<>();
        this.draws = new ArrayList<>();
        this.amountOfGames = new ArrayList<>();

        this.epochs = 0;

        df = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    }

    public Statistics(int wins, int loss, int draw, int amountOfGames) {
        this();
        add(wins, loss, draw, amountOfGames);
    }

    public void add(int wins, int loss, int draw, int amountOfGames) {
        this.wins.add(wins);
        this.loss.add(loss);
        this.draws.add(draw);
        this.amountOfGames.add(amountOfGames);

        this.epochs++;
    }

    public void append(Statistics statistics) {
        this.wins.addAll(statistics.getWins());
        this.loss.addAll(statistics.getLoss());
        this.draws.addAll(statistics.getDraws());
        this.amountOfGames.addAll(statistics.getAmountOfGames());

        this.epochs += statistics.getEpochs();
    }

    public boolean combine(Statistics statistics) {
        if (epochs == 0) {
            append(statistics);
            return true;
        } else if (epochs != statistics.getEpochs()) {
            return false;
        }

        for (int i = 0; i < epochs; i++) {
            wins.set(i, getWin(i) + statistics.getWin(i));
            loss.set(i, getLose(i) + statistics.getLose(i));
            draws.set(i, getDraw(i) + statistics.getDraw(i));
            amountOfGames.set(i, getAmountOfGames(i) + statistics.getAmountOfGames(i));
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < epochs; i++) {
            result += entryToString(i) + "\n";
        }
        return result;
    }

    public String formatColor(double x) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_DARK_RED = "\u001B[0;31m";
        String ANSI_BRIGHT_RED = "\u001B[1;31m";
        String ANSI_DARK_GREEN = "\u001B[0;32m";
        String ANSI_BRIGHT_GREEN = "\u001B[1;32m";
        String ANSI_DARK_YELLOW = "\u001B[0;33m";

        String result = "";
        if (x < -1) result += ANSI_BRIGHT_RED;
        else if (x < 0) result += ANSI_DARK_RED;
        else if (x == 0) result += ANSI_DARK_YELLOW;
        else if (x < 1) result += ANSI_DARK_GREEN;
        else result += ANSI_BRIGHT_GREEN;
        result += formatNumber(x);
        result += ANSI_RESET;
        return result;
    }

    public String formatNumber(double x) {
        return df.format(x);
    }

    public String entryToString(int i) {
        int amountOfSpaces = -30;
        String result = "";
        result += String.format("%" + amountOfSpaces + "s", "Win/lose/draw: " + getWin(i) + "/" + getLose(i) + "/" + getDraw(i));
        result += String.format("%" + amountOfSpaces + "s", "Win/games rate: " + formatNumber(getWinRate(i)));
        result += String.format("%" + amountOfSpaces + "s", "Win/games rate diff: " + getWinRateDiff(i));
//        result += String.format("%" + amountOfSpaces + "s", "Win/lose rate: " + getWinLoseRate(i));
//        result += String.format("%" + amountOfSpaces + "s", "Win/lose rate diff: " + formatColor(getWinLoseRateDiff(i)));
        return result;
    }

    public double getWinLoseRate(int index) {
        return 100 * wins.get(index) / (double) (amountOfGames.get(index) - draws.get(index));
    }

    public double getWinLoseRateDiff(int index) {
        if (index == 0) return 0;
        return getWinLoseRate(index) - getWinLoseRate(index - 1);
    }

    public double getWinRateLastEpoch() {
        return getWinRate(epochs - 1);
    }

    public double getWinRate(int index) {
        return 100 * wins.get(index) / (double) amountOfGames.get(index);
    }

    public double getWinRateDiff(int index) {
        if (index == 0) return 0;
        return getWinRate(index) - getWinRate(index - 1);
    }

    public int getWin(int index) {
        return wins.get(index);
    }

    public ArrayList<Integer> getWins() {
        return wins;
    }

    public int getLose(int index) {
        return loss.get(index);
    }

    public ArrayList<Integer> getLoss() {
        return loss;
    }

    public int getDraw(int index) {
        return draws.get(index);
    }

    public ArrayList<Integer> getDraws() {
        return draws;
    }

    public int getAmountOfGames(int index) {
        return amountOfGames.get(index);
    }

    public ArrayList<Integer> getAmountOfGames() {
        return amountOfGames;
    }

    public int getEpochs() {
        return epochs;
    }
}
