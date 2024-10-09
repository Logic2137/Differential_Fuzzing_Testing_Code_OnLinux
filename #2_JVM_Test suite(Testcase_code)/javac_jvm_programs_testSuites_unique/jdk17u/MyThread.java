
package nsk.jvmti.scenarios.hotswap.HS202.hs202t002;

public class MyThread extends Thread {

    private int val = 100;

    public void run() {
        playWithThis();
    }

    public void playWithThis() {
        try {
            display();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private synchronized void display() throws Exception {
        val += 10;
        throw new Exception(" Dummy Exception...");
    }

    public int getValue() {
        return val;
    }
}
