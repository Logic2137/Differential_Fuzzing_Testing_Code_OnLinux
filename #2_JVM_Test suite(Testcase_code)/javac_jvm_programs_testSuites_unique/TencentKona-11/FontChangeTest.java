



import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class FontChangeTest {
    static Frame frame;
    static Panel p1;
    static TextField tf1;
    static boolean failed = false;

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                 try {
                     testFont1();
                 } catch (StackOverflowError soe) {
                     failed = true;
                 }
            }
        });
        frame.dispose();
        if (failed) {
            throw new Exception("Test failed");
        }
    }

    private static void testFont1() {
        frame = new Frame();
        frame.setLayout(new BorderLayout());
        p1 = new Panel();
        p1.setLayout(new FlowLayout());
        tf1 = new TextField("ABC");
        tf1.setFont(new Font("Dialog", Font.PLAIN, 12));
        p1.add(tf1);
        frame.add(p1, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        p1.setVisible(false);
        tf1.setText("xyz");
        tf1.setFont(new Font("Dialog", Font.PLAIN, 24));
        p1.setVisible(true);
        frame.pack();
    }
}
