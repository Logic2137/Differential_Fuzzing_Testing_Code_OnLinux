

package nsk.jvmti.scenarios.events.EM02;

public class em02t003a extends Thread {

    public void run() {
        
        for (int i = 0; i < 1000; i++) {
            javaMethod(i);
        }
    }

    public int javaMethod(int i) {
        int k = 0;
        for (int j = 0; j < i; j++) {
            k += (i - j);
        }
        return k;
    }
}
