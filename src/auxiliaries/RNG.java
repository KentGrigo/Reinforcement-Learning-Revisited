package auxiliaries;

import java.util.Random;

public class RNG {

    private static final Random rng = new Random();

    public static int nextInt(int bound) {
        return rng.nextInt(bound);
    }

    public static double nextDouble() {
        return rng.nextDouble();
    }

    public static double nextGaussian() {
        return rng.nextGaussian();
    }
}
