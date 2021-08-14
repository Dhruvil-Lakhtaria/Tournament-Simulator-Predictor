package simulator.ui;
public final class Delay {

    private Delay() {
        throw new IllegalStateException("Cannot be instantiated"); //some exception
    }
    
    // this is for simple delay and no effect
    public static void makeDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    // loading delay but with control over time difference
    public static void specificDelay(int n, int x) {
        try {
            for(int i = 0;i<n;i++)
            {
            	System.out.print(".");
            	Thread.sleep(x);
            }
        }
        catch (InterruptedException e) {
        }
    }
}


