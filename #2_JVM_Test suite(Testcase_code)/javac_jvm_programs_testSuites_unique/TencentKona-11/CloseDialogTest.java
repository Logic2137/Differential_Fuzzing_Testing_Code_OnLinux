

import java.awt.Dialog;
import java.awt.Frame;
import java.io.*;
import javax.swing.*;
import sun.awt.SunToolkit;
import java.util.concurrent.atomic.AtomicReference;



public class CloseDialogTest {

    private static volatile Frame frame;
    private static volatile Dialog dialog;
    private static volatile InputStream testErrorStream;
    private static final PrintStream systemErrStream = System.err;
    private static final AtomicReference<Exception> caughtException
            = new AtomicReference<>();

    public static void main(String[] args) throws Exception {

        
        PipedOutputStream errorOutputStream = new PipedOutputStream();
        testErrorStream = new PipedInputStream(errorOutputStream);
        System.setErr(new PrintStream(errorOutputStream));

        ThreadGroup swingTG = new ThreadGroup(getRootThreadGroup(), "SwingTG");
        try {
            new Thread(swingTG, () -> {
                SunToolkit.createNewAppContext();
                SwingUtilities.invokeLater(() -> {
                    frame = new Frame();
                    frame.setSize(300, 300);
                    frame.setVisible(true);

                    dialog = new Dialog(frame);
                    dialog.setSize(200, 200);
                    dialog.setModal(true);
                    dialog.setVisible(true);
                });
            }).start();

            Thread.sleep(400);

            Thread disposeThread = new Thread(swingTG, () ->
                    SwingUtilities.invokeLater(() -> {
                try {
                    while (dialog == null || !dialog.isVisible()) {
                        Thread.sleep(100);
                    }
                    dialog.setVisible(false);
                    dialog.dispose();
                    frame.dispose();
                } catch (Exception e) {
                    caughtException.set(e);
                }
            }));
            disposeThread.start();
            disposeThread.join();
            Thread.sleep(500);

            
            final char[] buffer = new char[2048];
            System.err.print("END");
            System.setErr(systemErrStream);
            try (Reader in = new InputStreamReader(testErrorStream, "UTF-8")) {
                int size = in.read(buffer, 0, buffer.length);
                String errorString = new String(buffer, 0, size);
                if (!errorString.startsWith("END")) {
                    System.err.println(errorString.
                            substring(0, errorString.length() - 4));
                    throw new RuntimeException("Error output is not empty!");
                }
            }
        } finally {
            if (caughtException.get() != null) {
                throw new RuntimeException("Failed. Caught exception!",
                        caughtException.get());
            }
        }
    }

    private static ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }
}
