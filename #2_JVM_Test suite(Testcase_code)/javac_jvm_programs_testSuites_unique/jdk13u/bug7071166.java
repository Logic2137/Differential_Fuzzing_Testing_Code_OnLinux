import java.awt.Container;
import javax.swing.JButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.SwingConstants.EAST;
import static javax.swing.SwingConstants.NORTH;
import static javax.swing.SwingConstants.NORTH_EAST;
import static javax.swing.SwingConstants.NORTH_WEST;
import static javax.swing.SwingConstants.SOUTH;
import static javax.swing.SwingConstants.SOUTH_EAST;
import static javax.swing.SwingConstants.SOUTH_WEST;
import static javax.swing.SwingConstants.WEST;

public class bug7071166 {

    private static final int[] POSITIONS = { 
    NORTH, 
    EAST, 
    SOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, 123, -456 };

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            try {
                UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
            } catch (final UnsupportedLookAndFeelException ignored) {
                continue;
            }
            System.out.println("LookAndFeel: " + lookAndFeelInfo.getName());
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    LayoutStyle layoutStyle = LayoutStyle.getInstance();
                    System.out.println("LayoutStyle: " + layoutStyle);
                    for (int i = 0; i < POSITIONS.length; i++) {
                        int position = POSITIONS[i];
                        try {
                            layoutStyle.getPreferredGap(new JButton(), new JButton(), LayoutStyle.ComponentPlacement.RELATED, position, new Container());
                            if (i > 3) {
                                throw new RuntimeException("IllegalArgumentException is not thrown for position " + position);
                            }
                        } catch (IllegalArgumentException e) {
                            if (i <= 3) {
                                throw new RuntimeException("IllegalArgumentException is thrown for position " + position);
                            }
                        }
                    }
                }
            });
            System.out.println("passed");
        }
    }
}
