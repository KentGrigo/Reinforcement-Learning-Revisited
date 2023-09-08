package auxiliaries;

public class Counter {

    private int counter;

    public Counter() {
        counter = 0;
    }

    public synchronized int incrAndGet() {
        return ++counter;

    }
}
