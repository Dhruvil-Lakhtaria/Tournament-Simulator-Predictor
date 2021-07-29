public final class Delay {
    private Delay() {
        throw new IllegalStateException("Cannot be instantiated"); //some exception
    }
//this is for simple delay and no effect
    public static void makeDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }
//    this is for showing delay with loading effect....
    public static void loadingDelay(int n) {
        try {
            for(int i = 0;i<n;i++)
            {
            	System.out.print(".");
            	Thread.sleep(1000);
            }
            System.out.println();/*to move to new line*/
        } catch (InterruptedException e) {
        }
    }

    public static void specificDelay(int n, int x) {
        try {
            for(int i = 0;i<n;i++)
            {
            	System.out.print(".");
            	Thread.sleep(x);
            }
            // System.out.println();
        }
        catch (InterruptedException e) {
        }
    }
}


