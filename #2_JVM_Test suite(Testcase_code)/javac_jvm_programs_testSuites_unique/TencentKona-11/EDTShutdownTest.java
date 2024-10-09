



import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import sun.awt.AWTAccessor;

public class EDTShutdownTest {

    private static boolean passed = false;

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            
            EventQueue queue = Toolkit.getDefaultToolkit()
                               .getSystemEventQueue();
            Thread thread = AWTAccessor.getEventQueueAccessor()
                            .getDispatchThread(queue);
            try {
                
                Method stopDispatching = thread.getClass()
                        .getDeclaredMethod("stopDispatching", null);
                stopDispatching.setAccessible(true);
                stopDispatching.invoke(thread, null);

                
                EventQueue.invokeLater(() -> {
                    passed = true;
                });
            }
            catch (InvocationTargetException | NoSuchMethodException
                   | IllegalAccessException e) {
            }
        });

        
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        Thread thread = AWTAccessor.getEventQueueAccessor()
                        .getDispatchThread(queue);
        try {
            thread.join();

            
            thread = AWTAccessor.getEventQueueAccessor()
                     .getDispatchThread(queue);
            if (thread != null) {
                thread.join();
            }
        }
        catch (InterruptedException e) {
        }

        if (passed) {
            System.out.println("Test PASSED!");
        }
        else {
            throw new RuntimeException("Test FAILED!");
        }
    }
}
