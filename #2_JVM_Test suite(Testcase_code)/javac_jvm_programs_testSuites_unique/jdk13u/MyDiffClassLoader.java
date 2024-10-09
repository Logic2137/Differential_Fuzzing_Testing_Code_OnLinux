
package myloaders;

import java.io.*;
import java.lang.module.ModuleReference;

public class MyDiffClassLoader extends ClassLoader {

    public static MyDiffClassLoader loader1 = new MyDiffClassLoader();

    public static MyDiffClassLoader loader2 = new MyDiffClassLoader();

    public Class loadClass(String name) throws ClassNotFoundException {
        if (!name.equals("p1.c1") && !name.equals("p1.c1ReadEdgeDiffLoader") && !name.equals("p1.c1Loose") && !name.equals("p2.c2") && !name.equals("p3.c3") && !name.equals("p3.c3ReadEdgeDiffLoader") && !name.equals("c4") && !name.equals("c5") && !name.equals("p6.c6")) {
            return super.loadClass(name);
        }
        if ((name.equals("p2.c2") || name.equals("c4") || name.equals("p6.c6")) && (this == MyDiffClassLoader.loader1)) {
            return MyDiffClassLoader.loader2.loadClass(name);
        }
        byte[] data = getClassData(name);
        return defineClass(name, data, 0, data.length);
    }

    byte[] getClassData(String name) {
        try {
            String TempName = name.replaceAll("\\.", "/");
            String currentDir = System.getProperty("test.classes");
            String filename = currentDir + File.separator + TempName + ".class";
            FileInputStream fis = new FileInputStream(filename);
            byte[] b = new byte[5000];
            int cnt = fis.read(b, 0, 5000);
            byte[] c = new byte[cnt];
            for (int i = 0; i < cnt; i++) c[i] = b[i];
            return c;
        } catch (IOException e) {
            return null;
        }
    }

    public void register(ModuleReference mref) {
    }
}
