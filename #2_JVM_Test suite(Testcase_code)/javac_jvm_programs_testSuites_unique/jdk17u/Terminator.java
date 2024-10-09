
package nsk.share;

public class Terminator {

    protected Terminator() {
    }

    private static Thread terminator = null;

    public static int parseAppointment(String[] args) {
        int timeout = -1, margin = 1;
        int timeouts = 0, waittimes = 0, margins = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].startsWith("-waittime=")) {
                    timeout = Integer.parseInt(args[i].substring(10));
                    waittimes++;
                }
                if (args[i].startsWith("-margin=")) {
                    margin = Integer.parseInt(args[i].substring(8));
                    margins++;
                }
            } else {
                if (i == 0) {
                    timeout = Integer.parseInt(args[i]);
                    timeouts++;
                }
            }
        }
        ;
        if (timeouts == 0 && waittimes == 0)
            throw new IllegalArgumentException("no $TIMEOUT, nor -waittime=$WAITTIME is set");
        if (waittimes > 1)
            throw new IllegalArgumentException("more than one -waittime=... is set");
        if (margins > 1)
            throw new IllegalArgumentException("more than one -margin=... is set");
        int result = timeout - margin;
        if (result <= 0)
            throw new IllegalArgumentException("delay appointment must be greater than " + margin + " minutes");
        return result;
    }

    public static void appoint(int minutes) {
        appoint(minutes, 95);
    }

    public static void appoint(int minutes, int status) {
        if (terminator != null)
            throw new IllegalStateException("Terminator is already appointed.");
        final long timeToExit = System.currentTimeMillis() + 60 * 1000L * minutes;
        final int exitStatus = status;
        terminator = new Thread(Terminator.class.getName()) {

            public void run() {
                long timeToSleep = timeToExit - System.currentTimeMillis();
                if (timeToSleep > 0)
                    try {
                        Object someDummyObject = new Object();
                        synchronized (someDummyObject) {
                            someDummyObject.wait(timeToSleep);
                        }
                    } catch (InterruptedException exception) {
                        exception.printStackTrace(System.err);
                        return;
                    }
                ;
                System.err.println("#\n# Terminator: prescheduled program termination.\n#");
                System.exit(exitStatus);
            }
        };
        terminator.setPriority(Thread.MAX_PRIORITY);
        terminator.setDaemon(true);
        terminator.start();
    }
}
