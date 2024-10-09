import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

public class TestAbstractRegionPainter {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestAbstractRegionPainter::testAbstractRegionPainter);
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(TestAbstractRegionPainter::testAbstractRegionPainter);
    }

    private static void testAbstractRegionPainter() {
        UserAbstractRegionPainter painter = new UserAbstractRegionPainter();
        JComponent userComponent = new UserJComponent();
        Color color = painter.getUserComponentColor(userComponent, "UserColor", Color.yellow, 0, 0, 0);
        if (!UserJComponent.USER_COLOR.equals(color)) {
            throw new RuntimeException("Wrong color: " + color);
        }
    }

    public static class UserJComponent extends JComponent {

        public static final Color USER_COLOR = new Color(10, 20, 30);

        public static final Color TEST_COLOR = new Color(15, 25, 35);

        Color color = USER_COLOR;

        public UserJComponent() {
        }

        public Color getUserColor() {
            return color;
        }

        public void setUserColor(Color color) {
            this.color = color;
        }
    }

    public static class UserAbstractRegionPainter extends AbstractRegionPainter {

        public Color getUserComponentColor(JComponent c, String property, Color defaultColor, float saturationOffset, float brightnessOffset, int alphaOffset) {
            return getComponentColor(c, property, defaultColor, saturationOffset, brightnessOffset, alphaOffset);
        }

        @Override
        protected AbstractRegionPainter.PaintContext getPaintContext() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
