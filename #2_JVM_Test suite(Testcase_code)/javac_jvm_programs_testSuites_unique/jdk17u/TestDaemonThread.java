import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestDaemonThread implements Runnable {

    File classpath;

    public TestDaemonThread(File classpath) {
        this.classpath = classpath;
    }

    @Override
    public void run() {
        try {
            URL u = this.getClass().getClassLoader().getResource("DummyClass.class");
            String path = u.getPath();
            String parent = u.getPath().substring(0, path.lastIndexOf('/') + 1);
            URL parentURL = new URL(u, parent);
            System.out.println(parentURL);
            for (; ; ) {
                ClassLoader cl = new URLClassLoader(new URL[] { parentURL }, null);
                cl.loadClass("DummyClass");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new TestDaemonThread(new File(args[0])));
        t.setDaemon(true);
        t.start();
        Thread.sleep(200);
    }
}
