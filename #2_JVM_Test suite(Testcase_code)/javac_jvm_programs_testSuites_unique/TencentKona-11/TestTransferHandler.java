
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import java.awt.datatransfer.*;


public class TestTransferHandler {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestTransferHandler::testTransferHandler);
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(TestTransferHandler::testTransferHandler);
    }

    private static void testTransferHandler() {
        try {
            TransferHandler transferHandler = new TransferHandler("userColor");
            UserJComponent comp = new UserJComponent();

            final String colorType = DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=java.awt.Color";
            final DataFlavor colorFlavor = new DataFlavor(colorType);

            Transferable transferable = new Transferable() {

                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{colorFlavor};

                }

                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return true;
                }

                public Object getTransferData(DataFlavor flavor) {
                    return UserJComponent.TEST_COLOR;
                }

            };

            transferHandler.importData(comp, transferable);

            if (!UserJComponent.TEST_COLOR.equals(comp.color)) {
                throw new RuntimeException("Wrong color!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}
