

import java.awt.Point;
import java.awt.Window;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.lang.InterruptedException;
import java.lang.System;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;



public class FullScreenAfterSplash {

    private static JFrame frame;

    private static volatile boolean windowEnteringFullScreen = false;
    private static volatile boolean windowEnteredFullScreen = false;

    public static void main(String[] args) throws Exception {

        try {
            
            Robot r = new Robot();
            r.setAutoDelay(50);
            r.mouseMove(0, 0);
            sleep();

            SwingUtilities.invokeAndWait(FullScreenAfterSplash::createAndShowGUI);
            sleep();

            Point fullScreenButtonPos = frame.getLocation();
            if(System.getProperty("os.version").equals("10.9"))
                fullScreenButtonPos.translate(frame.getWidth() - 10, frame.getHeight()/2);
            else
                fullScreenButtonPos.translate(55,frame.getHeight()/2);
            r.mouseMove(fullScreenButtonPos.x, fullScreenButtonPos.y);

            
            int waitCount = 0;
            while (!windowEnteringFullScreen) {
                r.mousePress(InputEvent.BUTTON1_MASK);
                r.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(100);
                if (waitCount++ > 10) {
                    System.err.println("Can't enter full screen mode. Failed.");
                    System.exit(1);
                }
            }

            waitCount = 0;
            while (!windowEnteredFullScreen) {
                Thread.sleep(100);
                if (waitCount++ > 10) {
                    System.err.println("Can't enter full screen mode. Failed.");
                    System.exit(1);
                }
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame(" Fullscreen OSX Bug ");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        enableFullScreen(frame);
        frame.setBounds(100, 100, 100, 100);
        frame.pack();
        frame.setVisible(true);
    }

    
    private static void enableFullScreen(Window window) {
        try {
            Class<?> fullScreenUtilities = Class.forName("com.apple.eawt.FullScreenUtilities");
            Method setWindowCanFullScreen = fullScreenUtilities.getMethod("setWindowCanFullScreen", Window.class, boolean.class);
            setWindowCanFullScreen.invoke(fullScreenUtilities, window, true);
            Class fullScreenListener = Class.forName("com.apple.eawt.FullScreenListener");
            Object listenerObject = Proxy.newProxyInstance(fullScreenListener.getClassLoader(), new Class[]{fullScreenListener}, (proxy, method, args) -> {
                switch (method.getName()) {
                    case "windowEnteringFullScreen":
                        windowEnteringFullScreen = true;
                        break;
                    case "windowEnteredFullScreen":
                        windowEnteredFullScreen = true;
                        break;
                }
                return null;
            });
            Method addFullScreenListener = fullScreenUtilities.getMethod("addFullScreenListenerTo", Window.class, fullScreenListener);
            addFullScreenListener.invoke(fullScreenUtilities, window, listenerObject);
        } catch (Exception e) {
            throw new RuntimeException("FullScreen utilities not available", e);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }
    }
}
