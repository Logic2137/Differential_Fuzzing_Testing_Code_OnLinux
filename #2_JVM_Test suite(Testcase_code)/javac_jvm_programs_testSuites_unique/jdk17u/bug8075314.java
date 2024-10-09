import javax.swing.*;
import java.beans.PropertyVetoException;

public class bug8075314 {

    private static JFrame frame;

    private static JInternalFrame frame1;

    private static JInternalFrame frame2;

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        frame = new JFrame();
                        frame.setUndecorated(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        setup(frame);
                    }
                });
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            frame1.setMaximum(true);
                            frame1.setClosed(true);
                            if (frame2.isMaximum()) {
                                throw new RuntimeException("Frame2 is maximized!");
                            }
                        } catch (PropertyVetoException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } finally {
                if (frame != null) {
                    frame.dispose();
                }
            }
        }
        System.out.println("ok");
    }

    private static void setup(JFrame frame) {
        JDesktopPane desktop = new JDesktopPane();
        frame.setContentPane(desktop);
        frame1 = new JInternalFrame("1", true, true, true, true);
        frame1.setBounds(40, 40, 300, 200);
        frame1.setVisible(true);
        desktop.add(frame1);
        frame2 = new JInternalFrame("2", true, true, true, true);
        frame2.setBounds(20, 20, 300, 200);
        frame2.setVisible(true);
        desktop.add(frame2);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}
