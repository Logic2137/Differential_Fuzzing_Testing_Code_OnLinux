

import java.awt.Choice;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public final class SetBoundsPaintTest {

    public static void main(String[] args) throws Exception {
        CountDownLatch go = new CountDownLatch(10);
        Choice choice = new Choice();
        choice.addItem("first");
        choice.addItem("second");
        Frame frame = new Frame() {
            @Override
            public void paint(Graphics g) {
                g.fillRect(0, 0, 200, 200);
                choice.setBounds(50, 50, 180, 30);
                go.countDown();
            }
        };
        try {
            frame.setLayout(null);
            frame.add(choice);
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            if (go.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("recursive setBounds paint");
            }
        } finally {
            frame.dispose();
        }
    }
}
