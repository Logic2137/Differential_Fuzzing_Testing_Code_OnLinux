



import java.lang.reflect.*;
import javax.management.remote.*;
import javax.management.remote.rmi.*;
import java.io.*;

public class ObjectInputStreamWithLoaderNullCheckTest {

    private static Class<?> innerClass;

    public static void main(String[] args) throws Exception {

       System.out.println(">> == ObjectInputStreamWithLoaderNullCheckTest started...");

       try {
           innerClass = Class.forName("javax.management.remote.rmi.RMIConnector$ObjectInputStreamWithLoader");
           Constructor<?> ctor = innerClass.getDeclaredConstructor(InputStream.class,ClassLoader.class);
           ctor.setAccessible(true);

           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           ObjectOutput objOut =  new ObjectOutputStream(baos);
           objOut.writeObject(new String("Serialize"));
           objOut.close();
           baos.close();
           ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

           System.out.println(">> == Testing constructor with null class loader.");
           Object obj = ctor.newInstance(bais,null);

           System.out.println(">> == Test case failed. No error occured");
           System.exit(1);
       } catch (InvocationTargetException ex) {
           Throwable cause = ex.getCause();
           System.out.println(">> == InvocationTargetException Cause message : " + cause.toString());
           if (cause instanceof IllegalArgumentException) {
              System.out.println(">> == Test case Passed.");
           } else {
              System.out.println(">> == Test case Failed.");
              ex.printStackTrace();
              System.exit(1);
           }
       } catch (Exception ex) {
           System.out.println(">>> == Test case failed with error " + ex.getCause().getMessage());
           ex.printStackTrace();
           System.exit(1);
       }
    }
}
