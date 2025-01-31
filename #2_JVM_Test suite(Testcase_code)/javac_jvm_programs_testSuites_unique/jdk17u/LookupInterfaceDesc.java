import java.io.*;

interface Foo extends Serializable {

    @SuppressWarnings("serial")
    static final long serialVersionUID = 0xCAFE;
}

interface Bar extends Externalizable {

    @SuppressWarnings("serial")
    static final long serialVersionUID = 0xBABE;
}

interface Gub {
}

public class LookupInterfaceDesc {

    public static void main(String[] args) throws Exception {
        ObjectStreamClass desc = ObjectStreamClass.lookup(Foo.class);
        if ((desc.getSerialVersionUID() != Foo.serialVersionUID) || (desc.getFields().length != 0)) {
            throw new Error();
        }
        desc = ObjectStreamClass.lookup(Bar.class);
        if ((desc.getSerialVersionUID() != Bar.serialVersionUID) || (desc.getFields().length != 0)) {
            throw new Error();
        }
        if (ObjectStreamClass.lookup(Gub.class) != null) {
            throw new Error();
        }
    }
}
