import java.awt.image.CropImageFilter;
import java.awt.image.ImageFilter;
import java.awt.image.PixelGrabber;
import java.awt.image.ReplicateScaleFilter;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.spi.NamingManager;

public class GenerifiedUses {

    static void foo() throws Exception {
        Properties props = new Properties();
        new InitialDirContext(props);
        new InitialContext(props);
        NamingManager.getObjectInstance(null, null, null, props);
        new CropImageFilter(0, 0, 0, 0).setProperties(props);
        new ImageFilter().setProperties(props);
        new PixelGrabber(null, 0, 0, 0, 0, false).setProperties(props);
        new ReplicateScaleFilter(1, 1).setProperties(props);
    }
}
