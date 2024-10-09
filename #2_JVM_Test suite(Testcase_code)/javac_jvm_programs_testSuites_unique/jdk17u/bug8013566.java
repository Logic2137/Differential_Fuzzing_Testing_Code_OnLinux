import javax.swing.*;

public class bug8013566 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                final JFrame frame = new JFrame();
                try {
                    frame.setUndecorated(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    test(frame);
                } finally {
                    frame.dispose();
                }
            }
        });
        System.out.println("ok");
    }

    static void test(JFrame frame) {
        JComponent c1 = new JButton("Label1");
        JComponent c2 = new JButton("Label22");
        JComponent c3 = new JButton("Label333");
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        panel.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addComponent(c1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 50, 200)).addComponent(c2)).addComponent(c3));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(c1).addComponent(c2).addComponent(c3));
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        if (c3.getX() != c1.getX() + c1.getWidth() + 50) {
            throw new RuntimeException("Gap between 1st and 3rd component is wrong");
        }
    }
}
