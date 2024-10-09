import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class bug8048506 {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        System.out.println("The test passed");
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("bug8048506");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        PopupFactory factory = PopupFactory.getSharedInstance();
        Popup popup1 = factory.getPopup(frame, new JLabel("Popup with owner"), 100, 100);
        popup1.show();
        Popup popup2 = factory.getPopup(null, new JLabel("Popup without owner"), 200, 200);
        popup2.show();
    }
}
