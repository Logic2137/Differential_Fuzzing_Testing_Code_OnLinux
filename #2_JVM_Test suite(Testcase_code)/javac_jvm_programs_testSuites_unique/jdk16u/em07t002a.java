

package nsk.jvmti.scenarios.events.EM07;

public class em07t002a extends Thread {

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
