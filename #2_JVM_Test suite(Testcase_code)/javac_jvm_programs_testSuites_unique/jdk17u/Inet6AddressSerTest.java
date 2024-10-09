import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamConstants;
import static java.io.ObjectStreamConstants.STREAM_MAGIC;
import static java.io.ObjectStreamConstants.TC_CLASSDESC;
import static java.io.ObjectStreamConstants.TC_ENDBLOCKDATA;
import static java.io.ObjectStreamConstants.TC_NULL;
import static java.io.ObjectStreamConstants.TC_OBJECT;
import java.net.Inet6Address;

public class Inet6AddressSerTest implements ObjectStreamConstants {

    static class PayloadTest {

        private static byte[] serialize(String className) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos)) {
                dos.writeShort(STREAM_MAGIC);
                dos.writeShort(5);
                dos.writeByte(TC_OBJECT);
                dos.writeByte(TC_CLASSDESC);
                dos.writeUTF(className);
                dos.writeLong(6880410070516793377L);
                dos.writeByte(2);
                dos.writeShort(0);
                dos.writeByte(TC_ENDBLOCKDATA);
                dos.writeByte(TC_NULL);
                return baos.toByteArray();
            }
        }

        private static Object deserialize(final byte[] buffer) throws IOException, ClassNotFoundException {
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer))) {
                return ois.readObject();
            }
        }

        void test(String className) throws IOException, ClassNotFoundException {
            deserialize(serialize(className));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            new PayloadTest().test(Inet6Address.class.getName());
            throw new RuntimeException("Expected exception not raised");
        } catch (InvalidObjectException ioe) {
            if (ioe.getMessage().contains("invalid address length")) {
                System.out.println(String.format("Got expected exception: %s", ioe));
            } else {
                throw new RuntimeException("Expected exception not raised");
            }
        } catch (RuntimeException re) {
            throw new RuntimeException("Expected exception not raised");
        }
    }
}
