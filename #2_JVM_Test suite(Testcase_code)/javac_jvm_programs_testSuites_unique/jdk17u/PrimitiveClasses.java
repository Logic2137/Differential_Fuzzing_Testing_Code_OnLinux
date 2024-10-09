import java.io.*;

public class PrimitiveClasses {

    public static void main(String[] args) throws Exception {
        Class<?>[] primClasses = { boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class };
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        for (int i = 0; i < primClasses.length; i++) {
            oout.writeObject(primClasses[i]);
        }
        oout.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream oin = new ObjectInputStream(bin);
        for (int i = 0; i < primClasses.length; i++) {
            Object obj = oin.readObject();
            if (obj != primClasses[i]) {
                throw new Error("expected " + primClasses[i] + " instead of " + obj);
            }
        }
    }
}
