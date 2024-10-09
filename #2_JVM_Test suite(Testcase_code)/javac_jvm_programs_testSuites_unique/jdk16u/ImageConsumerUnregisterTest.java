



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ImageConsumerUnregisterTest extends javax.swing.JFrame {

    public static void main(String[] args) throws Exception {

        final java.awt.Component component = new ImageConsumerUnregisterTest();

        
        
        ByteArrayOutputStream rs = new ByteArrayOutputStream();
        PrintStream obj = System.err;
        System.setErr(new PrintStream(rs));

        String str = "";

        try {
            
            component.getToolkit().createCustomCursor(
                    component.getGraphicsConfiguration().createCompatibleImage(
                            16, 16, java.awt.Transparency.BITMASK),
                            new java.awt.Point(0, 0), "Hidden");

            
            str = rs.toString();
        } finally {
            
            System.setErr(obj);

            if (!str.isEmpty()) {
                throw new RuntimeException("Invalid"
                        + " imageComplete(STATICIMAGEDONE) call");
            }
        }
    }
}

