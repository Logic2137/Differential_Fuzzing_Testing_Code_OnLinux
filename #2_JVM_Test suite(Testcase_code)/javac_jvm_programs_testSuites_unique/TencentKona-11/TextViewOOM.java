

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class TextViewOOM {

    private static JFrame frame;
    private static JTextArea ta;
    private static final String STRING = "\uDC00\uD802\uDFFF";
    private static final int N = 5000;

    private static void createAndShowGUI() {
        frame = new JFrame();
        final JScrollPane jScrollPane1 = new JScrollPane();
        ta = new JTextArea();

        ta.setEditable(false);
        ta.setColumns(20);
        ta.setRows(5);
        jScrollPane1.setViewportView(ta);
        frame.add(ta);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        
        EventQueue.invokeAndWait(TextViewOOM::createAndShowGUI);
        for (int i = 0; i < 10; i++) {
            System.gc();
            Thread.sleep(1000);
        }
        long mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("Memory before creating the text: "+mem);
        final StringBuilder sb = new StringBuilder(N * STRING.length());
        for (int i = 0; i < N; i++) {
            sb.append(STRING);
        }
        for (int i = 0; i < 10; i++) {
            System.gc();
            Thread.sleep(1000);
        }
        mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("Memory after  creating the text: "+mem);

        EventQueue.invokeAndWait(() -> {
            ta.setText(sb.toString());
            for (int i = 0; i < 10; i++) {
                System.gc();
                try {Thread.sleep(200);} catch (InterruptedException iex) {}
            }
            long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.err.println("Memory after  setting the text: " + mem1);
        });
        for (int i = 0; i < 10; i++) {
            System.gc();
            Thread.sleep(1000);
        }
        mem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("Final memory  after everything: " + mem);
        EventQueue.invokeAndWait(frame::dispose);
    }
}
