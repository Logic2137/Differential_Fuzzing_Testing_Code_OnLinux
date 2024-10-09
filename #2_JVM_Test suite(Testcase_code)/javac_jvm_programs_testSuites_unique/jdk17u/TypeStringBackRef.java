import java.io.*;

public class TypeStringBackRef implements Serializable {

    private static final long serialVersionUID = 1L;

    String a, b, c, d, e, f, g;

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(ObjectStreamClass.lookup(TypeStringBackRef.class));
        oout.close();
        if (bout.size() != 116) {
            throw new Error("Wrong data length: " + bout.size());
        }
    }
}
