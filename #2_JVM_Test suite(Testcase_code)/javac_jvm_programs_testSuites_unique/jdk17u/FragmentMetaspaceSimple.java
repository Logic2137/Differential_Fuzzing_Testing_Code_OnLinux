import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentMetaspaceSimple {

    public static void main(String... args) {
        runSimple(Long.valueOf(System.getProperty("time", "80000")));
        System.gc();
    }

    private static void runSimple(long time) {
        long startTime = System.currentTimeMillis();
        ArrayList<ClassLoader> cls = new ArrayList<>();
        char sep = File.separatorChar;
        String fileName = "test" + sep + "Empty.class";
        File file = new File(fileName);
        byte[] buff = read(file);
        int i = 0;
        for (i = 0; System.currentTimeMillis() < startTime + time; ++i) {
            ClassLoader ldr = new MyClassLoader(buff);
            if (i % 1000 == 0) {
                cls.clear();
            }
            if (i % 2 == 1) {
                cls.add(ldr);
            }
            Class<?> c = null;
            try {
                c = ldr.loadClass("test.Empty");
                c.getClass().getClassLoader();
            } catch (ClassNotFoundException ex) {
                System.out.println("i=" + i + ", len" + buff.length);
                throw new RuntimeException(ex);
            }
            c = null;
        }
        cls = null;
        System.out.println("Finished " + i + " iterations in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static byte[] read(File file) {
        byte[] buff = new byte[(int) (file.length())];
        try {
            DataInputStream din = new DataInputStream(new FileInputStream(file));
            din.readFully(buff);
            din.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return buff;
    }

    static class MyClassLoader extends ClassLoader {

        byte[] buff;

        MyClassLoader(byte[] buff) {
            this.buff = buff;
        }

        public Class<?> loadClass() throws ClassNotFoundException {
            String name = "test.Empty";
            try {
                return defineClass(name, buff, 0, buff.length);
            } catch (Throwable e) {
                throw new ClassNotFoundException(name, e);
            }
        }
    }
}
