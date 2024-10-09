import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class CheckDisplayModes {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice graphicDevice : ge.getScreenDevices()) {
            if (!graphicDevice.isDisplayChangeSupported()) {
                System.err.println("Display mode change is not supported on this host. Test is considered passed.");
                continue;
            }
            DisplayMode defaultDisplayMode = graphicDevice.getDisplayMode();
            checkDisplayMode(defaultDisplayMode);
            graphicDevice.setDisplayMode(defaultDisplayMode);
            DisplayMode[] displayModes = graphicDevice.getDisplayModes();
            boolean isDefaultDisplayModeIncluded = false;
            for (DisplayMode displayMode : displayModes) {
                checkDisplayMode(displayMode);
                graphicDevice.setDisplayMode(displayMode);
                if (defaultDisplayMode.equals(displayMode)) {
                    isDefaultDisplayModeIncluded = true;
                }
            }
            if (!isDefaultDisplayModeIncluded) {
                throw new RuntimeException("Default display mode is not included");
            }
        }
    }

    static void checkDisplayMode(DisplayMode displayMode) {
        if (displayMode == null || displayMode.getWidth() <= 1 || displayMode.getHeight() <= 1) {
            throw new RuntimeException("invalid display mode");
        }
    }
}
