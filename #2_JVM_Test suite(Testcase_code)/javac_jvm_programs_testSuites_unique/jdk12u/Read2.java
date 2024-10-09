



import java.io.*;

class Foo implements Serializable {
    private static final long serialVersionUID = 0L;

    boolean z;
    byte b;
    char c;
    short s;
    int i;
    long j;
    float f;
    double d;
    String str;
    Object extra;

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        ObjectInputStream.GetField fields = in.readFields();
        if ((fields.get("z", false) != true) ||
            (fields.get("b", (byte) 0) != 5) ||
            (fields.get("c", '0') != '5') ||
            (fields.get("s", (short) 0) != 5) ||
            (fields.get("i", 0) != 5) ||
            (fields.get("j", 0l) != 5) ||
            (fields.get("f", 0.0f) != 5.0f) ||
            (fields.get("d", 0.0) != 5.0) ||
            (! fields.get("str", null).equals("5")))
        {
            throw new Error();
        }
    }
}

public class Read2 {
    public static void main(String[] args) throws Exception {
        ObjectInputStream oin =
            new ObjectInputStream(new FileInputStream("tmp.ser"));
        oin.readObject();
        oin.close();
    }
}
