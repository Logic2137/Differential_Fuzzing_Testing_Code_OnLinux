import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class Util {

    public static void convertRectToScreen(Rectangle r, Component c) {
        Point p = new Point(r.x, r.y);
        SwingUtilities.convertPointToScreen(p, c);
        r.x = p.x;
        r.y = p.y;
    }

    public static boolean compareBufferedImages(BufferedImage bufferedImage0, BufferedImage bufferedImage1) {
        int width = bufferedImage0.getWidth();
        int height = bufferedImage0.getHeight();
        if (width != bufferedImage1.getWidth() || height != bufferedImage1.getHeight()) {
            return false;
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bufferedImage0.getRGB(x, y) != bufferedImage1.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void generateOOME() {
        List<Object> bigLeak = new LinkedList<Object>();
        boolean oome = false;
        System.out.print("Filling the heap");
        try {
            for (int i = 0; true; i++) {
                bigLeak.add(new byte[1024 * 1024]);
                System.out.print(".");
                if (i % 10 == 0) {
                    System.gc();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            bigLeak = null;
            oome = true;
        }
        System.out.println("");
        if (!oome) {
            throw new RuntimeException("Problem with test case - never got OOME");
        }
        System.out.println("Got OOME");
    }

    public static Component findSubComponent(Component parent, String className) {
        String parentClassName = parent.getClass().getName();
        if (parentClassName.contains(className)) {
            return parent;
        }
        if (parent instanceof Container) {
            for (Component child : ((Container) parent).getComponents()) {
                Component subComponent = findSubComponent(child, className);
                if (subComponent != null) {
                    return subComponent;
                }
            }
        }
        return null;
    }

    public static void hitMnemonics(Robot robot, int... keys) {
        ArrayList<Integer> mnemonicKeyCodes = getSystemMnemonicKeyCodes();
        for (Integer mnemonic : mnemonicKeyCodes) {
            robot.keyPress(mnemonic);
        }
        hitKeys(robot, keys);
        for (Integer mnemonic : mnemonicKeyCodes) {
            robot.keyRelease(mnemonic);
        }
    }

    public static void hitKeys(Robot robot, int... keys) {
        for (int i = 0; i < keys.length; i++) {
            robot.keyPress(keys[i]);
        }
        for (int i = keys.length - 1; i >= 0; i--) {
            robot.keyRelease(keys[i]);
        }
    }

    public static void glide(Robot robot, int x0, int y0, int x1, int y1) throws AWTException {
        float dmax = (float) Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));
        float dx = (x1 - x0) / dmax;
        float dy = (y1 - y0) / dmax;
        for (int i = 0; i <= dmax; i += 10) {
            robot.mouseMove((int) (x0 + dx * i), (int) (y0 + dy * i));
        }
    }

    public static Point getCenterPoint(final Component component) throws Exception {
        return Util.invokeOnEDT(new Callable<Point>() {

            @Override
            public Point call() throws Exception {
                Point p = component.getLocationOnScreen();
                Dimension size = component.getSize();
                return new Point(p.x + size.width / 2, p.y + size.height / 2);
            }
        });
    }

    public static <T> T invokeOnEDT(final Callable<T> task) throws Exception {
        final List<T> result = new ArrayList<>(1);
        final Exception[] exception = new Exception[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    result.add(task.call());
                } catch (Exception e) {
                    exception[0] = e;
                }
            }
        });
        if (exception[0] != null) {
            throw exception[0];
        }
        return result.get(0);
    }

    public static ArrayList<Integer> getKeyCodesFromKeyMask(int modifiers) {
        ArrayList<Integer> result = new ArrayList<>();
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            result.add(KeyEvent.VK_CONTROL);
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            result.add(KeyEvent.VK_ALT);
        }
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            result.add(KeyEvent.VK_SHIFT);
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            result.add(KeyEvent.VK_META);
        }
        return result;
    }

    public static ArrayList<Integer> getSystemMnemonicKeyCodes() {
        String osName = System.getProperty("os.name");
        ArrayList<Integer> result = new ArrayList<>();
        if (osName.contains("OS X")) {
            result.add(KeyEvent.VK_CONTROL);
        }
        result.add(KeyEvent.VK_ALT);
        return result;
    }

    public static JDialog createModalDialogWithPassFailButtons(final String failString) {
        JDialog retDialog = new JDialog();
        Box buttonBox = Box.createHorizontalBox();
        JButton passButton = new JButton("Pass");
        JButton failButton = new JButton("Fail");
        passButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                retDialog.dispose();
            }
        });
        failButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                retDialog.dispose();
                throw new RuntimeException("Test failed. " + failString);
            }
        });
        retDialog.setTitle("Test");
        retDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        buttonBox.add(passButton);
        buttonBox.add(Box.createGlue());
        buttonBox.add(failButton);
        retDialog.getContentPane().add(buttonBox, BorderLayout.SOUTH);
        retDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return retDialog;
    }
}
