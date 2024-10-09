

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class bug4966171 {

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            EventQueue.invokeAndWait(bug4966171::test);
        }
    }

    private static void test() {
        
        long endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < endtime) {
            try {
                var byteOut = new ByteArrayOutputStream();
                try (var out = new ObjectOutputStream(byteOut)) {
                    out.writeObject(new JFileChooser());
                }
                var byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                try (var in = new ObjectInputStream(byteIn)) {
                    JFileChooser readFc = (JFileChooser) in.readObject();
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
