

package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;


public class IAE_Loader2 extends SecureClassLoader {

    
    private final ClassLoader toClone;

    
    private final HashSet<String> notLoadable;

    
    private final HashSet<String> simpleDelegate;

    
    public IAE_Loader2(String name, ClassLoader parent, ClassLoader toClone,
                       String[] notLoadable, String[] simpleDelegate) {
        super(name, parent);

        this.toClone = toClone;
        this.notLoadable    = Arrays.stream(notLoadable).collect(Collectors.toCollection(HashSet<String>::new));
        this.simpleDelegate = Arrays.stream(simpleDelegate).collect(Collectors.toCollection(HashSet<String>::new));
    }

    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (notLoadable.contains(name)) {
            throw new ClassNotFoundException("The clone class loader explicitely " +
                    "didn't found the class");
        }

        if (simpleDelegate.contains(name)) {
            return toClone.loadClass(name);
        }

        
        URL res = toClone.getResource(name.replace('.', '/') + ".class");

        if (res == null) {
            throw new ClassNotFoundException(name);
        }

        try {
            InputStream is = res.openStream();
            byte[] code = readStreamIntoBuffer(is, 8192);
            is.close();
            return defineClass(name, code, 0, code.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    
    public static byte[] readStreamIntoBuffer(InputStream is, int chunkSize)
            throws IOException {

        
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize <= 0");
        }
        else if (is == null) {
            throw new NullPointerException("is is null");
        }

        
        byte[] tempBuffer = new byte[chunkSize];
        byte[] buffer     = new byte[0];

        int bytesRead = 0;  
        int oldSize   = 0;  

        while ((bytesRead = is.read(tempBuffer)) > 0) {

            
            byte[] oldBuffer = buffer;

            
            buffer = new byte[oldSize + bytesRead];
            System.arraycopy(oldBuffer,  0, buffer, 0,       oldBuffer.length);

            
            System.arraycopy(tempBuffer, 0, buffer, oldSize, bytesRead);
            oldSize += bytesRead;
        }

        return buffer;
    }
}
