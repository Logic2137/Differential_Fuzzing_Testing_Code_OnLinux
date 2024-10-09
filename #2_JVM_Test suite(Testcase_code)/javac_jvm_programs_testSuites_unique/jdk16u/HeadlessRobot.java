

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Robot;


public final class HeadlessRobot {

    public static void main(String[] args) {
        try {
            new Robot();
            throw new RuntimeException("Expected AWTException did not occur");
        } catch (AWTException ignored) {
            
        }

        try {
            new Robot(new GraphicsDevice() {
                @Override
                public int getType() {
                    return TYPE_RASTER_SCREEN;
                }

                @Override
                public String getIDstring() {
                    return "Stub device";
                }

                @Override
                public GraphicsConfiguration[] getConfigurations() {
                    return null;
                }

                @Override
                public GraphicsConfiguration getDefaultConfiguration() {
                    return null;
                }
            });
            throw new RuntimeException("Expected AWTException did not occur");
        } catch (AWTException ignored) {
            
        }
    }
}
