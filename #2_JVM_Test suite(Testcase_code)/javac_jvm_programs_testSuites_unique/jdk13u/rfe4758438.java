import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;

public class rfe4758438 implements PropertyChangeListener {

    enum PROPS {

        drag_threshold("org.gnome.settings-daemon.peripherals.mouse drag-threshold", "/desktop/gnome/peripherals/mouse/drag_threshold", "gnome.Net/DndDragThreshold", "int", new String[] { "5", "6" }), double_click("org.gnome.settings-daemon.peripherals.mouse double-click", "/desktop/gnome/peripherals/mouse/double_click", "gnome.Net/DoubleClickTime", "int", new String[] { "200", "300" }), cursor_blink("org.gnome.desktop.interface cursor-blink", "/desktop/gnome/interface/cursor_blink", "gnome.Net/CursorBlink", "bool", new String[] { "true", "false" }), cursor_blink_time("org.gnome.desktop.interface cursor-blink-time", "/desktop/gnome/interface/cursor_blink_time", "gnome.Net/CursorBlinkTime", "int", new String[] { "1000", "1500" }), gtk_theme("org.gnome.desktop.interface gtk-theme", "/desktop/gnome/interface/gtk_theme", "gnome.Net/ThemeName", "string", new String[] { "Crux", "Simple" });

        public final String gsettings;

        public final String gconftool;

        public final String java;

        public final String type;

        public final String[] values;

        PROPS(String gsettings, String gconftool, String java, String type, String[] values) {
            this.gsettings = gsettings;
            this.gconftool = gconftool;
            this.java = java;
            this.type = type;
            this.values = values;
        }
    }

    static boolean useGsettings;

    static String tool;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    String changedProperty;

    Object changedValue;

    Object lock = new Object();

    public void propertyChange(PropertyChangeEvent event) {
        changedProperty = event.getPropertyName();
        changedValue = toolkit.getDesktopProperty(changedProperty);
        System.out.println("Property " + changedProperty + " changed. Changed value: " + changedValue);
        synchronized (lock) {
            try {
                lock.notifyAll();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        useGsettings = System.getProperty("useGsettings").equals("true");
        tool = System.getProperty("tool");
        String osName = System.getProperty("os.name");
        if (!"Linux".equals(osName) && !"SunOS".equals(osName))
            System.out.println("This test need not be run on this platform");
        else
            new rfe4758438().doTest();
    }

    void doTest() throws Exception {
        for (PROPS p : PROPS.values()) toolkit.addPropertyChangeListener(p.java, this);
        for (PROPS p : PROPS.values()) {
            Thread.sleep(1000);
            doTest(p);
        }
        System.out.println("Test passed");
    }

    void doTest(PROPS property) throws Exception {
        Object obj = toolkit.getDesktopProperty(property.java);
        if (obj == null)
            throw new RuntimeException("No such property available: " + property.java);
        if (property.type.equals("bool")) {
            if (obj.equals(new Integer(1))) {
                obj = new String("true");
            } else {
                obj = new String("false");
            }
        }
        Object value = property.values[0];
        if (obj.toString().equals(value)) {
            value = property.values[1];
        }
        StringBuffer sb = new StringBuffer(tool);
        if (useGsettings) {
            sb.append(" set ");
            sb.append(property.gsettings);
            sb.append(" ");
        } else {
            sb.append(" --set --type=");
            sb.append(property.type);
            sb.append(" ");
            sb.append(property.gconftool);
            sb.append(" ");
        }
        String tempCommand = sb.toString();
        sb.append(value.toString());
        changedProperty = "";
        changedValue = null;
        if (executeCommand(sb.toString()) != 0)
            throw new RuntimeException("Could not execute the command");
        synchronized (lock) {
            try {
                lock.wait(5000);
            } catch (Exception e) {
            }
        }
        if (property.type.equals("bool")) {
            if (changedValue.equals(new Integer(1))) {
                changedValue = new String("true");
            } else {
                changedValue = new String("false");
            }
        }
        if (!changedProperty.equals(property.java)) {
            executeCommand(tempCommand + obj.toString());
            throw new RuntimeException("PropertyChangedEvent did not occur for " + property.java);
        } else if (!changedValue.toString().equals(value.toString())) {
            executeCommand(tempCommand + obj.toString());
            throw new RuntimeException("New value of the property is different from " + "the value supplied");
        }
        executeCommand(tempCommand + obj.toString());
    }

    int executeCommand(String command) throws Exception {
        System.out.println("Executing " + command);
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        InputStream es = process.getErrorStream();
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        process.waitFor();
        while (is.available() > 0) stdout.append((char) is.read());
        while (es.available() > 0) stderr.append((char) es.read());
        if (stdout.length() > 0)
            System.out.println(stdout.toString());
        if (stderr.length() > 0)
            System.err.println(stderr.toString());
        return process.exitValue();
    }
}
