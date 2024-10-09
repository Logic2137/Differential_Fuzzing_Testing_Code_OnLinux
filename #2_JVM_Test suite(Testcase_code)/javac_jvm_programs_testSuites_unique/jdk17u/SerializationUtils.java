
package jdk.test.lib.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializationUtils {

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bs)) {
            out.writeObject(obj);
        }
        return bs.toByteArray();
    }

    public static Object deserialize(byte[] ba) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(ba))) {
            return in.readObject();
        }
    }

    private SerializationUtils() {
    }
}
