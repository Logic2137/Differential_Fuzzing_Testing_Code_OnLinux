

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AppForegroundEvent;
import java.awt.desktop.AppHiddenEvent;
import java.awt.desktop.AppReopenedEvent;
import java.awt.desktop.OpenFilesEvent;
import java.awt.desktop.OpenURIEvent;
import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PrintFilesEvent;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.ScreenSleepEvent;
import java.awt.desktop.SystemSleepEvent;
import java.awt.desktop.UserSessionEvent;
import java.awt.desktop.UserSessionEvent.Reason;
import java.io.File;
import java.util.Collections;
import java.util.List;


public final class DesktopEventsExceptions {

    public static void main(final String[] args) {
        
        final List<Runnable> constructors = List.of(
                AboutEvent::new,
                AppForegroundEvent::new,
                AppHiddenEvent::new,
                AppReopenedEvent::new,
                QuitEvent::new,
                ScreenSleepEvent::new,
                SystemSleepEvent::new,
                PreferencesEvent::new,
                () -> new PrintFilesEvent(Collections.emptyList()),
                () -> new UserSessionEvent(Reason.UNSPECIFIED),
                () -> new OpenFilesEvent(Collections.emptyList(), ""),
                () -> new OpenURIEvent(new File("").toURI())
        );

        for (final Runnable test : constructors) {
            try {
                test.run();
                checkHeadless(true);
                checkSupported(true);
            } catch (HeadlessException ex) {
                checkHeadless(false);
            } catch (UnsupportedOperationException ex) {
                checkSupported(false);
            }
        }
    }

    private static void checkSupported(final boolean isSupported) {
        if (isSupported != Desktop.isDesktopSupported()) {
            throw new RuntimeException();
        }
    }

    private static void checkHeadless(final boolean isHeadless) {
        if (isHeadless == GraphicsEnvironment.isHeadless()) {
            throw new RuntimeException();
        }
    }
}
