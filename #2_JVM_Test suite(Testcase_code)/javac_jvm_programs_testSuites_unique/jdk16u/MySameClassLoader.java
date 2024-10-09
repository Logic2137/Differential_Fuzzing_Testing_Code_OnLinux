
package myloaders;

import java.io.*;
import java.lang.module.ModuleReference;




public class MySameClassLoader extends ClassLoader
{
    public static MySameClassLoader loader1 = new MySameClassLoader();

    public Class loadClass(String name) throws ClassNotFoundException {
        
        if (!name.equals("p1.c1") &&
            !name.equals("p1.c1ReadEdge") &&
            !name.equals("p1.c1Loose") &&
            !name.equals("p2.c2") &&
            !name.equals("p3.c3") &&
            !name.equals("p3.c3ReadEdge") &&
            !name.equals("c4") &&
            !name.equals("c5") &&
            !name.equals("p6.c6")) {
            return super.loadClass(name);
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
           for (int i=0; i<cnt; i++) c[i] = b[i];
              return c;
        } catch (IOException e) {
           return null;
        }
    }

    public void register(ModuleReference mref) { }
}
