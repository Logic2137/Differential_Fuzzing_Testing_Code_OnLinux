import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.zip.*;

public class Test7068051 {

    public static void main(String[] args) throws Throwable {
        ZipFile zf = new ZipFile(args[0]);
        Enumeration<? extends ZipEntry> entries = zf.entries();
        ArrayList<String> names = new ArrayList<String>();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (entries.hasMoreElements()) {
            names.add(entries.nextElement().getName());
        }
        byte[] bytes = new byte[16];
        for (String name : names) {
            ZipEntry e = zf.getEntry(name);
            if (e.isDirectory())
                continue;
            final InputStream is = zf.getInputStream(e);
            try {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                while (is.read(bytes) >= 0) {
                }
                is.close();
            } catch (IOException x) {
                System.out.println("..................................");
                System.out.println("          -->  is :" + is);
                System.out.println("          is.hash :" + is.hashCode());
                System.out.println();
                System.out.println("           e.name :" + e.getName());
                System.out.println("           e.hash :" + e.hashCode());
                System.out.println("         e.method :" + e.getMethod());
                System.out.println("           e.size :" + e.getSize());
                System.out.println("          e.csize :" + e.getCompressedSize());
                x.printStackTrace();
                System.out.println("..................................");
                System.exit(97);
            }
        }
        zf.close();
    }
}
