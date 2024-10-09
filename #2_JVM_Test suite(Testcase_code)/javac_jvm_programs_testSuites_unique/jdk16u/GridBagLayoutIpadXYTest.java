



import java.awt.*;

public class GridBagLayoutIpadXYTest {
    static Frame frame = new Frame();
    static TextField jtf = null;
    static final int customIpadx = 300;
    static final int customIpady = 40;

    public static void main(final String[] args) {
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        Insets fieldInsets = new Insets(0,5,5,0);

        gc.anchor = gc.NORTH;
        gc.fill = gc.HORIZONTAL;
        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.ipadx = customIpadx;
        gc.ipady = customIpady;
        gc.insets = fieldInsets;
        jtf = new TextField();
        frame.add(jtf, gc);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Robot robot;
        try {
            robot = new Robot();
            robot.waitForIdle();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }

        Dimension minSize = jtf.getMinimumSize();
        if ( minSize.width + customIpadx != jtf.getSize().width ||
             minSize.height + customIpady != jtf.getSize().height ){
            System.out.println("TextField originally has min size = " + jtf.getMinimumSize());
            System.out.println("TextField supplied with ipadx =  300, ipady =40");
            System.out.println("Frame size: " + frame.getSize());
            System.out.println(" Fields's size is "+jtf.getSize());

            throw new RuntimeException("Test Failed. TextField has incorrect width. ");
        }
        System.out.println("Test Passed.");
    }
}
