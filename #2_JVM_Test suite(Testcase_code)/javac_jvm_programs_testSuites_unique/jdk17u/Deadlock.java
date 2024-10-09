import java.security.*;

public class Deadlock implements Runnable {

    private volatile Exception exc;

    public void run() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            System.out.println("getInstance() ok: " + random);
        } catch (Exception e) {
            System.out.println("Exception during getInstance() call: " + e);
            this.exc = e;
        }
    }

    public static void main(String[] args) throws Exception {
        Deadlock d = new Deadlock();
        Thread t = new Thread(d);
        t.start();
        String className = (args.length == 0) ? "com.abc.Tst1" : args[0];
        System.out.println("Loading class: " + className);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println("SystemClassLoader: " + cl);
        Class clazz = cl.loadClass(className);
        System.out.println("OK: " + clazz);
        t.join();
        if (d.exc != null) {
            throw d.exc;
        }
    }
}
