import java.io.*;
import java.lang.reflect.*;

public class MethodTest implements Serializable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        Method readObject = ObjectInputStream.class.getDeclaredMethod("readObject", new Class<?>[0]);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(new MethodTest());
        oout.close();
        for (int i = 0; i < 100; i++) {
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
            readObject.invoke(oin, new Object[0]);
        }
    }
}
