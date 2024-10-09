



import java.util.*;

public class KillThread {
    static volatile Thread tdThread;
    public static void main (String[] args) throws Exception  {
        Timer t = new Timer();

        
        t.schedule(new TimerTask() {
            public void run() {
                tdThread = Thread.currentThread();
                throw new ThreadDeath();
            }
        }, 0);

        
        try {
            do {
                Thread.sleep(100);
            } while(tdThread == null);
        } catch(InterruptedException e) {
        }
        tdThread.join();

        
        try {
            
            t.schedule(new TimerTask() {
                public void run() {
                }
            }, 0);
            throw new Exception("We failed silently");
        } catch(IllegalStateException e) {
            
        }
    }
}
