public class MonitorCacheMaybeExpand_DeadLock {

    static class LotsaMonitors extends Thread {

        static final int MAX_DEPTH = 800;

        int depth = 0;

        int tid;

        public LotsaMonitors(int tid, int depth) {
            super("LotsaMonitors #" + new Integer(tid).toString());
            this.tid = tid;
            this.depth = depth;
        }

        public void run() {
            System.out.println(">>>Starting " + this.toString() + " ...");
            Thread.currentThread().yield();
            this.recurse();
            System.out.println("<<<Finished " + this.toString());
        }

        synchronized void recurse() {
            if (this.depth > 0) {
                new LotsaMonitors(tid, depth - 1).recurse();
            }
        }
    }

    public static void main(String[] args) {
        new LotsaMonitors(1, LotsaMonitors.MAX_DEPTH).start();
        new LotsaMonitors(2, LotsaMonitors.MAX_DEPTH).start();
        for (int i = 0; i < MAX_GC_ITERATIONS; i++) {
            new LotsaMonitors(i + 3, LotsaMonitors.MAX_DEPTH).start();
            System.out.println(">>>Loading 10 classes and gc'ing ...");
            Class[] classes = new Class[10];
            fillClasses(classes);
            classes = null;
            System.gc();
            Thread.currentThread().yield();
            System.out.println("<<<Finished loading 10 classes and gc'ing");
        }
    }

    static final int MAX_GC_ITERATIONS = 10;

    static void fillClasses(Class[] classes) {
        for (int i = 0; i < classes.length; i++) {
            try {
                classes[i] = Class.forName(classnames[i]);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        }
    }

    private static String[] classnames = { "java.text.DecimalFormat", "java.text.MessageFormat", "java.util.GregorianCalendar", "java.util.ResourceBundle", "java.text.Collator", "java.util.Date", "java.io.Reader", "java.io.Writer", "java.lang.IllegalAccessException", "java.lang.InstantiationException", "java.lang.ClassNotFoundException", "java.lang.CloneNotSupportedException", "java.lang.InterruptedException", "java.lang.NoSuchFieldException", "java.lang.NoSuchMethodException", "java.lang.RuntimeException", "java.lang.ArithmeticException", "java.lang.ArrayStoreException", "java.lang.ClassCastException", "java.lang.StringIndexOutOfBoundsException", "java.lang.NegativeArraySizeException", "java.lang.IllegalStateException", "java.lang.IllegalArgumentException", "java.lang.NumberFormatException", "java.lang.IllegalThreadStateException", "java.lang.IllegalMonitorStateException", "java.lang.SecurityException", "java.lang.ExceptionInInitializerError" };
}
