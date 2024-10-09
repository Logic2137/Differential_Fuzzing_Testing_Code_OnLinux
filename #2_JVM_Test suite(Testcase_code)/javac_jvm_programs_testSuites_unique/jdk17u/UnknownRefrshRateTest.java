import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class UnknownRefrshRateTest {

    public static void main(String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();
        for (GraphicsDevice d : devices) {
            if (!d.isDisplayChangeSupported()) {
                continue;
            }
            DisplayMode odm = d.getDisplayMode();
            System.out.println("device=" + d + " original mode=" + odm);
            DisplayMode[] modes = d.getDisplayModes();
            System.out.println("There are " + modes.length + " modes.");
            try {
                for (int i = 0; i < modes.length; i++) {
                    DisplayMode mode = modes[i];
                    System.out.println("copying from mode " + i + " : " + mode);
                    int w = mode.getWidth();
                    int h = mode.getHeight();
                    int bpp = mode.getBitDepth();
                    int refRate = DisplayMode.REFRESH_RATE_UNKNOWN;
                    DisplayMode newMode = new DisplayMode(w, h, bpp, refRate);
                    d.setDisplayMode(newMode);
                    Thread.sleep(2000);
                    System.out.println("set " + d.getDisplayMode());
                }
            } finally {
                System.out.println("restoring original mode" + odm);
                d.setDisplayMode(odm);
                Thread.sleep(10000);
            }
        }
    }
}
