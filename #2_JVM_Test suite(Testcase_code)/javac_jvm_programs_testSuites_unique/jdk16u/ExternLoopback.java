



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
        throws IOException, ClassNotFoundException
    {
        return descs.removeFirst();
    }
}

public class ExternLoopback implements Externalizable {
    private static final long serialVersionUID = 1L;

    String a, b, c;

    public ExternLoopback() {
    }

    ExternLoopback(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(false);
        out.writeObject(a);
        out.writeObject(b);
        out.writeObject(c);
    }

    public void readExternal(ObjectInput in)
        throws IOException, ClassNotFoundException
    {
        in.readBoolean();
        a = (String) in.readObject();
        b = (String) in.readObject();
        c = (String) in.readObject();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ExternLoopback)) {
            return false;
        }
        ExternLoopback other = (ExternLoopback) obj;
        return streq(a, other.a) && streq(b, other.b) && streq(c, other.c);
    }

    public int hashCode() {
        return a.hashCode();
    }

    static boolean streq(String s1, String s2) {
        return (s1 != null) ? s1.equals(s2) : (s2 == null);
    }

    public static void main(String[] args) throws Exception {
        ExternLoopback lb = new ExternLoopback("foo", "bar", "baz");
        LinkedList<ObjectStreamClass> descs = new LinkedList<>();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        LoopbackOutputStream lout = new LoopbackOutputStream(bout, descs);
        lout.writeObject(lb);
        lout.close();

        LoopbackInputStream lin = new LoopbackInputStream(
            new ByteArrayInputStream(bout.toByteArray()), descs);
        ExternLoopback lbcopy = (ExternLoopback) lin.readObject();
        if (!lb.equals(lbcopy)) {
            throw new Error();
        }
    }
}
