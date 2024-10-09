



import java.lang.ref.Reference;

public class DynamicCodeGeneratedTest {
    static {
        System.loadLibrary("DynamicCodeGenerated");
    }
    public static native void changeEventNotificationMode();

    public static void main(String[] args) {
        
        
        Thread t = new Thread(() -> {
            changeEventNotificationMode();
        });
        t.setDaemon(true);
        t.start();

        for (int i = 0; i < 2000; i++) {
            new Thread(() -> {
                String result = "string" + System.currentTimeMillis();
                Reference.reachabilityFence(result);
            }).start();
        }
    }
}
