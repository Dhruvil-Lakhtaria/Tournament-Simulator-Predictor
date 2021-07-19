public final class Delay {
    private Delay() {
        throw new IllegalStateException("Cannot be instantiated"); //some exception
    }

    public static void makeDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {

        }
    }
}


