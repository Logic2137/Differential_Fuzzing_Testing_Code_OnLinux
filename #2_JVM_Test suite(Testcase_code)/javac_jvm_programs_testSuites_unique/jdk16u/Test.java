
package p;

public class Test implements Runnable {
    private final Runnable r;
    public Test(Runnable r) {
        this.r = r;
    }

    
    public void run() {
        r.run();
    }
}
