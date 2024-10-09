import java.lang.instrument.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class RedefineBigClassAgent {

    private static int N_REDEFINES = 1000;

    public static Class clz;

    public static volatile boolean doneRedefining = false;

    public static void premain(String agentArgs, final Instrumentation inst) throws Exception {
        String s = agentArgs.substring(0, agentArgs.indexOf(".class"));
        clz = Class.forName(s.replace('/', '.'));
        ClassLoader loader = RedefineBigClassAgent.class.getClassLoader();
        URL classURL = loader.getResource(agentArgs);
        if (classURL == null) {
            throw new Exception("Cannot find class: " + agentArgs);
        }
        int redefineLength;
        InputStream redefineStream;
        System.out.println("Reading test class from " + classURL);
        if (classURL.getProtocol().equals("file")) {
            File f = new File(classURL.getFile());
            redefineStream = new FileInputStream(f);
            redefineLength = (int) f.length();
        } else {
            URLConnection conn = classURL.openConnection();
            redefineStream = conn.getInputStream();
            redefineLength = conn.getContentLength();
        }
        final byte[] buffer = new byte[redefineLength];
        new BufferedInputStream(redefineStream).read(buffer);
        System.gc();
        new Timer(true).schedule(new TimerTask() {

            public void run() {
                try {
                    int i;
                    System.out.println("Redefining");
                    ClassDefinition cld = new ClassDefinition(clz, buffer);
                    for (i = 0; i < N_REDEFINES; i++) {
                        inst.redefineClasses(new ClassDefinition[] { cld });
                        System.gc();
                    }
                    System.out.println("Redefined " + i + " times.");
                    RedefineBigClassAgent.doneRedefining = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
