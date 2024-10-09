import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeListenerTest implements PropertyChangeListener {

    Object property;

    Object lock = new Object();

    boolean propertyChanged = false;

    public static void main(String[] args) throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray not supported on the platform under test. " + "Marking the test passed");
        } else {
            new PropertyChangeListenerTest().doTest();
        }
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (!"trayIcons".equals(event.getPropertyName()))
            throw new RuntimeException("ERROR: PropertyName not matching. Event " + "triggered for a different property\n" + "Property: " + event.getPropertyName());
        property = event.getNewValue();
        propertyChanged = true;
        synchronized (lock) {
            try {
                lock.notifyAll();
            } catch (Exception e) {
            }
        }
    }

    void doTest() throws Exception {
        propertyChanged = false;
        SystemTray tray = SystemTray.getSystemTray();
        tray.addPropertyChangeListener(null, null);
        tray.addPropertyChangeListener("trayIcons", null);
        tray.addPropertyChangeListener("trayIcons", this);
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 32, 32);
        g.setColor(Color.RED);
        g.fillRect(6, 6, 20, 20);
        g.dispose();
        TrayIcon icon = new TrayIcon(img);
        if (propertyChanged)
            throw new RuntimeException("FAIL: spurious property events triggered");
        propertyChanged = false;
        tray.add(icon);
        if (!propertyChanged) {
            synchronized (lock) {
                try {
                    lock.wait(3000);
                } catch (Exception e) {
                }
            }
        }
        if (!propertyChanged) {
            throw new RuntimeException("FAIL: property event did not get triggered when tray icon added");
        } else {
            if (!(property instanceof TrayIcon[])) {
                throw new RuntimeException("FAIL: property is not TrayIcon[]. TrayIcon added.");
            } else {
                TrayIcon[] icons = (TrayIcon[]) property;
                if (icons.length != 1 || !icon.equals(icons[0])) {
                    throw new RuntimeException("FAIL: TrayIcon[] returned by the " + "PropertyChangeEvent is incorrect. TrayIcon added.\n" + "icon[] length: " + icons.length);
                }
            }
        }
        propertyChanged = false;
        tray.remove(icon);
        if (!propertyChanged) {
            synchronized (lock) {
                try {
                    lock.wait(3000);
                } catch (Exception e) {
                }
            }
        }
        if (!propertyChanged) {
            throw new RuntimeException("FAIL: property event did not get triggered when tray icon removed");
        } else {
            if (!(property instanceof TrayIcon[])) {
                throw new RuntimeException("FAIL: property is not TrayIcon[]. TrayIcon removed.");
            } else {
                TrayIcon[] icons = (TrayIcon[]) property;
                if (icons.length != 0) {
                    throw new RuntimeException("FAIL: TrayIcon[] returned by the " + "PropertyChangeEvent is incorrect. TrayIcon removed.\n" + "icon[] length: " + icons.length);
                }
            }
        }
        tray.removePropertyChangeListener("trayIcons", null);
        tray.removePropertyChangeListener("trayIcons", this);
        propertyChanged = false;
        tray.add(icon);
        Thread.sleep(3000);
        if (propertyChanged)
            throw new RuntimeException("FAIL: property listener notified even after " + "removing the listener from SystemTray. TrayIcon added.");
        propertyChanged = false;
        tray.remove(icon);
        Thread.sleep(3000);
        if (propertyChanged)
            throw new RuntimeException("FAIL: property listener notified even after " + "removing the listener from SystemTray. TrayIcon removed.");
        tray.addPropertyChangeListener("someName", this);
        propertyChanged = false;
        tray.add(icon);
        Thread.sleep(3000);
        if (propertyChanged)
            throw new RuntimeException("FAIL: property listener notified when " + "listener added for a different property. TrayIcon added.");
        propertyChanged = false;
        tray.remove(icon);
        Thread.sleep(3000);
        if (propertyChanged)
            throw new RuntimeException("FAIL: property listener notified when " + "listener added for a different property. TrayIcon removed.");
        tray.addPropertyChangeListener("trayIcons", this);
        tray.addPropertyChangeListener("trayIcons", this);
        PropertyChangeListener listener = event -> {
        };
        tray.addPropertyChangeListener("trayIcons", listener);
        tray.addPropertyChangeListener("sampleProp", event -> {
        });
        if (tray.getPropertyChangeListeners("trayIcons").length != 3) {
            throw new RuntimeException("FAIL: getPropertyChangeListeners did not return the " + "correct value for trayIcons property. Expected: 3, " + "Actual: " + tray.getPropertyChangeListeners("trayIcons").length);
        } else if (!this.equals(tray.getPropertyChangeListeners("trayIcons")[0]) || !this.equals(tray.getPropertyChangeListeners("trayIcons")[1]) || !listener.equals(tray.getPropertyChangeListeners("trayIcons")[2])) {
            throw new RuntimeException("FAIL: getPropertyChangeListeners did not return the " + "expected listeners\n" + "tray.getPropertyChangeListeners('trayIcons')[0] " + tray.getPropertyChangeListeners("trayIcons")[0] + "\n" + "tray.getPropertyChangeListeners('trayIcons')[1] " + tray.getPropertyChangeListeners("trayIcons")[1] + "\n" + "tray.getPropertyChangeListeners('trayIcons')[2] " + tray.getPropertyChangeListeners("trayIcons")[2]);
        }
        if (tray.getPropertyChangeListeners("sampleProp").length != 1)
            throw new RuntimeException("FAIL: getPropertyChangeListeners did not return the " + "expected listener for 'sampleProp'");
    }
}
