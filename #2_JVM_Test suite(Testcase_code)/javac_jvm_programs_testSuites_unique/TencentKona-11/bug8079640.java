




import javax.swing.*;
import java.awt.*;

public class bug8079640 {

    private static JFrame frame;
    private static JComponent comp2;

    public static void main(String[] args) throws Exception {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame = new JFrame("A Frame");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setUndecorated(true);
                    setup(frame);
                    frame.setVisible(true);
                }
            });

            test();
            System.out.println("ok");

        } finally {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.dispose();
                }
            });
        }
    }

    private static void test() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                if(comp2.getLocation().getY() > frame.getHeight())
                    throw new RuntimeException("GroupLayout fails: comp2 is out of the window");
            }
        });
    }


    static void setup(JFrame frame)  {
        JPanel panel = new JPanel();
        JComponent comp1 = new JLabel("Test Label 1");
        comp1.setMinimumSize(new Dimension(1000, 40000));
        comp1.setPreferredSize(new Dimension(1000, 40000));
        JScrollPane scroll = new JScrollPane(comp1);
        comp2 = new JLabel("Test Label 2");
        GroupLayout layout = new GroupLayout(panel);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scroll)
                        .addComponent(comp2));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(scroll)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comp2));
        panel.setLayout(layout);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(800, 600);
    }

}
