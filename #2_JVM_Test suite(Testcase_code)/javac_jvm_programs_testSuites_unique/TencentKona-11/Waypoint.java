

 
package test.java.awt.regtesthelpers;

public class Waypoint {

    static final String MSG = "Waypoint timed out";
    
    static final int TIMEOUT = 5000;
    boolean clear = false;

    public Waypoint() {

    }

    
    
    
    synchronized public void requireClear() throws RuntimeException {
        requireClear(MSG, TIMEOUT);
    }

    synchronized public void requireClear(long timeout)
            throws RuntimeException {
        requireClear(MSG, timeout);
    }

    synchronized public void requireClear(String timeOutMsg)
            throws RuntimeException {
        requireClear(timeOutMsg, TIMEOUT);
    }

    synchronized public void requireClear(String timeOutMsg, long timeout)
            throws RuntimeException {
        long endtime = System.currentTimeMillis() + timeout;
        try {
            while (isClear() == false) {
                if (System.currentTimeMillis() < endtime) {
                    wait(200);
                } else {
                    break;
                }
            }

            if (!isClear()) {
                throw new RuntimeException(timeOutMsg);
            }
        } catch (InterruptedException ix) {
        }
    }

    
    
    
    synchronized public void clear() {
        clear = true;
        notify();
    }

    
    
    
    
    synchronized public boolean isClear() {
        return clear;
    }

    synchronized public boolean isValid() {
        return clear;
    }

    
    
    
    synchronized public void reset() {
        clear = false;
    }

}
