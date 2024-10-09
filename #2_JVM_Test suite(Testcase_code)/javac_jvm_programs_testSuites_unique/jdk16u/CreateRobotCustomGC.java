

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Robot;


public final class CreateRobotCustomGC {

    public static void main(String[] args) {
        try {
            new Robot(new GraphicsDevice() {
                @Override
                public int getType() {
                    return TYPE_RASTER_SCREEN;
                }

                @Override
                public String getIDstring() {
                    return "Custom screen device";
                }

                @Override
                public GraphicsConfiguration[] getConfigurations() {
                    return new GraphicsConfiguration[0];
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
