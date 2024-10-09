



import java.awt.Frame;
import java.awt.Button;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.ContainerOrderFocusTraversalPolicy;

public class ContainerOrderFTPTest {

    private final ContainerOrderFocusTraversalPolicy coftp;
    private final Frame frame;
    private final Button b1;
    private final Button b2;
    private final String expectedTraversal;

    public ContainerOrderFTPTest() {
        expectedTraversal = "B1B2F1";
        b1 = new Button("B1");
        b2 = new Button("B2");
        frame = new Frame("F1");

        frame.setLayout(new FlowLayout());
        frame.setSize(200, 200);
        coftp = new ContainerOrderFocusTraversalPolicy();
        frame.setFocusTraversalPolicy(coftp);
        frame.add(b1);
        frame.add(b2);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        ContainerOrderFTPTest test = new ContainerOrderFTPTest();
        test.performTest();
        test.dispose();
    }

    public void performTest() {
        int count = 0;
        Component comp = coftp.getFirstComponent(frame);
        String traversal = "";
        do {
            comp = coftp.getComponentAfter(frame, comp);
            if (comp instanceof Button) {
                traversal += ((Button)comp).getLabel();
            } else if (comp instanceof Frame) {
                traversal += ((Frame)comp).getTitle();
            }
            count++;
        } while(count < 3);

        if (!expectedTraversal.equals(traversal)) {
            dispose();
            throw new RuntimeException("Incorrect Traversal. Expected : "
                + expectedTraversal + "Actual : " + traversal);
        }
    }

    public void dispose() {
        frame.dispose();
    }
}
