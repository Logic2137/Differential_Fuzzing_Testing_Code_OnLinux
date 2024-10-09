

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.*;
import java.util.concurrent.Callable;


public class TestLambdaFileEncodingSerialization {
        public static class ABCÃ¢ implements Serializable {
                public String msg;
                public ABCÃ¢() {
                        msg = "Hello world";
                }
                public static Callable<ABCÃ¢> getHello() {
                        return (Callable<ABCÃ¢> & Serializable) () -> {
                                return new ABCÃ¢();
                        };
                }
        }

    
    private static <T> T reserialize(T o) throws IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);

                oos.writeObject(o);

                oos.close();

                ObjectInputStream iis = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));

                try {
                        o = (T)iis.readObject();
                } catch (ClassNotFoundException e) {
                        throw new IOException(e);
                }
                iis.close();
                return o;
    }

    public static void main(String args[]) throws Exception{
        System.out.println("Default charset = "+Charset.defaultCharset());

        
        Callable<ABCÃ¢> foo = ABCÃ¢.getHello();
        ABCÃ¢ hello = new ABCÃ¢();

        
        ABCÃ¢ rh = reserialize(hello);
        System.out.println(rh.msg);

        
        reserialize(foo).call();
    }
}

