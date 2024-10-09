
package nsk.share.runner;

public class GCRunner implements Runnable {

    public void run() {
        System.gc();
    }
}
