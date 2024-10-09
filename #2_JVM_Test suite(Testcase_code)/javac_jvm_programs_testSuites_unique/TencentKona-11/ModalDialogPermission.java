

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Timer;
import java.util.TimerTask;


public final class ModalDialogPermission {

    public static void main(final String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                throw new RuntimeException(e);
            }
        });
        final Frame frame = new Frame();
        final Dialog dialog = new Dialog(frame, "ModalDialog", true);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }, 3000L);
        dialog.show();
        frame.dispose();
        t.cancel();
    }
}
