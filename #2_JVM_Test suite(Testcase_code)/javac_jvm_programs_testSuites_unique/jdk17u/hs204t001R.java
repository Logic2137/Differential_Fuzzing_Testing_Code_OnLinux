
package nsk.jvmti.scenarios.hotswap.HS204.hs204t001;

import java.util.concurrent.atomic.AtomicBoolean;

public class hs204t001R extends Thread {

    static public AtomicBoolean suspend = new AtomicBoolean(false);

    static public AtomicBoolean run = new AtomicBoolean(true);

    private int index = 0;

    public hs204t001R() {
        setName("hs204t001R");
        System.out.println("hs204t001R");
    }

    public void run() {
        System.out.println(" started running thread..");
        doInThisThread();
        System.out.println(" comming out ..");
    }

    private void doInThisThread() {
        System.out.println("... Inside doThisThread..");
        while (hs204t001R.run.get()) {
            index += 10;
            if (index == 1500) {
                hs204t001R.suspend.set(true);
            }
        }
    }

    public int getIndex() {
        return index;
    }
}
