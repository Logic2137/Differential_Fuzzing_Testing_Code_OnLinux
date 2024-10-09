



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;

public class TestObjectStreamClass {

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOutput);
        output.writeObject(new TestClass());

        ByteArrayInputStream bais = new ByteArrayInputStream(byteOutput.toByteArray());
        TestObjectInputStream input = new TestObjectInputStream(bais);
        input.readObject();

        ObjectStreamClass osc = input.getDescriptor();

        
        osc.getName();
        osc.forClass();
        osc.getField("str");
        osc.getFields();
        osc.getSerialVersionUID();
        osc.toString();
    }

    static class TestClass implements Serializable {
        private static final long serialVersionUID = 1L;

        String str = "hello world";
    }

    static class TestObjectInputStream extends ObjectInputStream {
        private ObjectStreamClass objectStreamClass;

        public TestObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        public ObjectStreamClass getDescriptor()
            throws IOException, ClassNotFoundException
        {
            return objectStreamClass;
        }

        public ObjectStreamClass readClassDescriptor()
            throws IOException, ClassNotFoundException
        {
            objectStreamClass = super.readClassDescriptor();
            return objectStreamClass;
        }
    }
}
