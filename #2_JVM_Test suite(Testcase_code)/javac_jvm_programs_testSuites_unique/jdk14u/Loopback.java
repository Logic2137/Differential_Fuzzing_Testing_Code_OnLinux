



import java.io.*;
import java.util.*;

class LoopbackOutputStream extends ObjectOutputStream {
    LinkedList<ObjectStreamClass> descs;

    LoopbackOutputStream(OutputStream out, LinkedList<ObjectStreamClass> descs)
        throws IOException
    {
        super(out);
        this.descs = descs;
    }

    protected void writeClassDescriptor(ObjectStreamClass desc)
        throws IOException
    {
        descs.add(desc);
    }
}

class LoopbackInputStream extends ObjectInputStream {
    LinkedList<ObjectStreamClass> descs;

    LoopbackInputStream(InputStream in, LinkedList<ObjectStreamClass> descs) throws IOException {
        super(in);
        this.descs = descs;
    }

    protected ObjectStreamClass readClassDescriptor()
    {
        return descs.removeFirst();
    }
}

public class Loopback implements Serializable {
    private static final long serialVersionUID = 1L;

    String str;

    Loopback(String str) {
        this.str = str;
    }

    public static void main(String[] args) throws Exception {
        Loopback lb = new Loopback("foo");
        LinkedList<ObjectStreamClass> descs = new LinkedList<>();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        LoopbackOutputStream lout = new LoopbackOutputStream(bout, descs);
        lout.writeObject(lb);
        lout.close();

        LoopbackInputStream lin = new LoopbackInputStream(
            new ByteArrayInputStream(bout.toByteArray()), descs);
        Loopback lbcopy = (Loopback) lin.readObject();
        if (!lb.str.equals(lbcopy.str)) {
            throw new Error();
        }
    }
}
