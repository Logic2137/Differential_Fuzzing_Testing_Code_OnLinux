

import java.awt.*;
import java.awt.image.BufferedImage;



public class HeadlessGraphicsDevice {
    public static void main(String args[]) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Graphics2D gd = ge.createGraphics(new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR));
        GraphicsConfiguration gc = gd.getDeviceConfiguration();
        GraphicsDevice gdev = gc.getDevice();

        for (GraphicsConfiguration gcl : gdev.getConfigurations())
            gcl.toString();

        gdev.getDefaultConfiguration().toString();
        gdev.getIDstring();

        if (gdev.getType() != GraphicsDevice.TYPE_IMAGE_BUFFER)
            throw new RuntimeException("Created GraphicsDevice that should be IMAGE_BUFFER but it isn't");
    }
}
