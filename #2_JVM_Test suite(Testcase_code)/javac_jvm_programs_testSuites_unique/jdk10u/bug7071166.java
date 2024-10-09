



import javax.swing.*;
import static javax.swing.SwingConstants.*;
import java.awt.*;

public class bug7071166 {
    private static final int[] POSITIONS = {NORTH, EAST, SOUTH, WEST, 
            NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, 123, -456}; 

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());

            System.out.println("LookAndFeel: " + lookAndFeelInfo.getName());

            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    LayoutStyle layoutStyle = LayoutStyle.getInstance();

                    System.out.println("LayoutStyle: " + layoutStyle);

                    for (int i = 0; i < POSITIONS.length; i++) {
                        int position = POSITIONS[i];

                        try {
                            layoutStyle.getPreferredGap(new JButton(), new JButton(),
                                    LayoutStyle.ComponentPlacement.RELATED, position, new Container());

                            if (i > 3) {
                                throw new RuntimeException("IllegalArgumentException is not thrown for position " +
                                        position);
                            }
                        } catch (IllegalArgumentException e) {
                            if (i <= 3) {
                                throw new RuntimeException("IllegalArgumentException is thrown for position " +
                                        position);
                            }
                        }
                    }
                }
            });

            System.out.println("passed");
        }
    }
}
