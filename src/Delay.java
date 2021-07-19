public class Delay {
    public static void main(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {

        }
    }
}

