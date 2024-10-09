



import java.security.*;
import java.nio.*;
import java.nio.channels.*;
import java.io.*;

public class DefineClassByteBuffer {

    static void test(ClassLoader cl) throws Exception {
        Class c = Class.forName("TestClass", true, cl);
        if (!"TestClass".equals(c.getName())) {
            throw new RuntimeException("Got wrong class: " + c);
        }
    }

    public static void main(String arg[]) throws Exception {

        
        
        

        File oldFile = new File(System.getProperty("test.classes", "."),
                                  "TestClass.class");
        File newFile = new File(System.getProperty("test.classes", "."),
                                  "CLAZZ");
        oldFile.renameTo(newFile);

        ClassLoader[] cls = new ClassLoader[DummyClassLoader.MAX_TYPE];
        for (int i = 0; i < cls.length; i++) {
            cls[i] = new DummyClassLoader(i);
        }

        
        for (int i = 0; i < cls.length; i++) {
            test(cls[i]);
        }

        if (DummyClassLoader.count != cls.length) {
             throw new Exception("DummyClassLoader not always used");
        }
    }

    
    public static class DummyClassLoader extends SecureClassLoader {

        public static final String CLASS_NAME = "TestClass";

        public static final int MAPPED_BUFFER = 0;
        public static final int DIRECT_BUFFER = 1;
        public static final int ARRAY_BUFFER = 2;
        public static final int WRAPPED_BUFFER = 3;
        public static final int READ_ONLY_ARRAY_BUFFER = 4;
        public static final int READ_ONLY_DIRECT_BUFFER = 5;
        public static final int DUP_ARRAY_BUFFER = 6;
        public static final int DUP_DIRECT_BUFFER = 7;
        public static final int MAX_TYPE = 7;

        int loaderType;

        static int count = 0;

        DummyClassLoader(int loaderType) {
            this.loaderType = loaderType;
        }

        static ByteBuffer[] buffers = new ByteBuffer[MAX_TYPE + 1];

        static ByteBuffer readClassFile(String name) {
            try {
                File f = new File(System.getProperty("test.classes", "."),
                                  "CLAZZ");
                try (FileInputStream fin = new FileInputStream(f);
                        FileChannel fc = fin.getChannel()) {
                    return fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Can't open file: " + name, e);
            } catch (IOException e) {
                throw new RuntimeException("Can't open file: " + name, e);
            }
        }

        static {
            
            buffers[MAPPED_BUFFER] = readClassFile(CLASS_NAME + ".class");
            byte[] array = new byte[buffers[MAPPED_BUFFER].limit()];
            buffers[MAPPED_BUFFER].get(array);
            buffers[MAPPED_BUFFER].flip();

            buffers[DIRECT_BUFFER] = ByteBuffer.allocateDirect(array.length);
            buffers[DIRECT_BUFFER].put(array);
            buffers[DIRECT_BUFFER].flip();

            buffers[ARRAY_BUFFER] = ByteBuffer.allocate(array.length);
            buffers[ARRAY_BUFFER].put(array);
            buffers[ARRAY_BUFFER].flip();

            buffers[WRAPPED_BUFFER] = ByteBuffer.wrap(array);

            buffers[READ_ONLY_ARRAY_BUFFER] = buffers[ARRAY_BUFFER].asReadOnlyBuffer();

            buffers[READ_ONLY_DIRECT_BUFFER] = buffers[DIRECT_BUFFER].asReadOnlyBuffer();

            buffers[DUP_ARRAY_BUFFER] = buffers[ARRAY_BUFFER].duplicate();

            buffers[DUP_DIRECT_BUFFER] = buffers[DIRECT_BUFFER].duplicate();
        }

         public Class findClass(String name) {
             CodeSource cs = null;
             count++;
             return defineClass(name, buffers[loaderType], cs);
         }
    } 

} 
