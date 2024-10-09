


package nsk.share.runner;


public class FinRunner implements Runnable {
        public void run() {
                System.runFinalization();
        }
}
