

import java.awt.Desktop;


public final class DefaultPermissions {

    public static void main(final String[] args) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop();
        }
    }
}
