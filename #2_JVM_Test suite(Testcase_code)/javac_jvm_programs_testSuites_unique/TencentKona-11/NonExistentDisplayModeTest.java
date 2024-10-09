

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.DisplayMode.REFRESH_RATE_UNKNOWN;


public class NonExistentDisplayModeTest {

    public static void main(String[] args) {
        new NonExistentDisplayModeTest().start();
    }

    private void start() {
        Frame f = new Frame("Testing, please wait..");
        f.pack();
        GraphicsDevice gd = f.getGraphicsConfiguration().getDevice();
        if (!gd.isFullScreenSupported()) {
            System.out.println("Exclusive FS mode not supported, test passed.");
            f.dispose();
            return;
        }

        gd.setFullScreenWindow(f);
        if (!gd.isDisplayChangeSupported()) {
            System.out.println("DisplayMode change not supported, test passed.");
            f.dispose();
            return;
        }

        DisplayMode dms[] = gd.getDisplayModes();
        ArrayList<DisplayMode> dmList = new ArrayList<DisplayMode>(dms.length);
        for (DisplayMode dm : dms) {
            dmList.add(dm);
        }

        ArrayList<DisplayMode> nonExistentDms = createNonExistentDMList(dmList);

        for (DisplayMode dm : nonExistentDms) {
            boolean exThrown = false;
            try {
                System.out.printf("Testing mode: (%4dx%4d) depth=%3d rate=%d\n",
                                  dm.getWidth(), dm.getHeight(),
                                  dm.getBitDepth(), dm.getRefreshRate());
                gd.setDisplayMode(dm);
            } catch (IllegalArgumentException e) {
                exThrown = true;
            }
            if (!exThrown) {
                gd.setFullScreenWindow(null);
                f.dispose();
                throw new
                    RuntimeException("Failed: No exception thrown for dm "+dm);
            }
        }
        gd.setFullScreenWindow(null);
        f.dispose();
        System.out.println("Test passed.");
    }

    private static final Random rnd = new Random();
    private ArrayList<DisplayMode>
        createNonExistentDMList(ArrayList<DisplayMode> dmList)
    {
        ArrayList<DisplayMode> newList =
            new ArrayList<DisplayMode>(dmList.size());
        
        int param = 0;
        for (DisplayMode dm : dmList) {
            param = ++param % 3;
            switch (param) {
                case 0: {
                    DisplayMode newDM = deriveSize(dm);
                    if (!dmList.contains(newDM)) {
                        newList.add(newDM);
                    }
                    break;
                }
                case 1: {
                    DisplayMode newDM = deriveDepth(dm);
                    if (!dmList.contains(newDM)) {
                        newList.add(newDM);
                    }
                    break;
                }
                case 2: {
                    if (dm.getRefreshRate() != REFRESH_RATE_UNKNOWN) {
                        DisplayMode newDM = deriveRR(dm);
                        if (!dmList.contains(newDM)) {
                            newList.add(newDM);
                        }
                    }
                    break;
                }
            }
        }
        return newList;
    }

    private static DisplayMode deriveSize(DisplayMode dm) {
        int w = dm.getWidth() / 7;
        int h = dm.getHeight() / 3;
        return new DisplayMode(w, h, dm.getBitDepth(), dm.getRefreshRate());
    }
    private static DisplayMode deriveRR(DisplayMode dm) {
        return new DisplayMode(dm.getWidth(), dm.getHeight(),
                               dm.getBitDepth(), 777);
    }
    private static DisplayMode deriveDepth(DisplayMode dm) {
        int depth;
        if (dm.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI) {
            depth = 77;
        } else {
            depth = DisplayMode.BIT_DEPTH_MULTI;
        }
        return new DisplayMode(dm.getWidth(), dm.getHeight(),
                               depth, dm.getRefreshRate());
    }
}
