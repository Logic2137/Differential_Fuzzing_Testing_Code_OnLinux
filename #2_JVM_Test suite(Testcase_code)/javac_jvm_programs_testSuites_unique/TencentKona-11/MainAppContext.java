



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import sun.awt.*;

public class MainAppContext {

    public static void main(String[] args) {
        ThreadGroup secondGroup = new ThreadGroup("test");
        new Thread(secondGroup, () -> {
            SunToolkit.createNewAppContext();
            test(true);
        }).start();

        
        
        try { Thread.sleep(2000); } catch (Exception e) {}

        test(false);
    }

    private static void test(boolean expectAppContext) {
        boolean appContextIsCreated = AppContext.getAppContext() != null;
        if (expectAppContext != appContextIsCreated) {
            throw new RuntimeException("AppContext is created: " + appContextIsCreated
                                                 + " expected: " + expectAppContext);
        }
    }
}
