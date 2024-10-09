

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public class PeekInputStreamTest {

    public static void main(String[] args) throws ReflectiveOperationException,
            IOException {

        InputStream pin = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        peek(pin);
        if (pin.skip(1) != 1 || pin.read() != 2)
            throw new AssertionError();

        InputStream pin1 = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        if (pin1.skip(1) != 1 || pin1.read() != 2)
            throw new AssertionError();

        InputStream pin2 = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        if (pin2.skip(0) != 0 || pin2.read() != 1)
            throw new AssertionError();

        InputStream pin3 = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        if (pin3.skip(2) != 2 || pin3.read() != 3)
            throw new AssertionError();

        InputStream pin4 = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        if (pin4.skip(3) != 3 || pin4.read() != 4)
            throw new AssertionError();

        InputStream pin5 = createPeekInputStream(
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4}));
        if (pin5.skip(16) != 4 || pin5.read() != -1)
            throw new AssertionError();
    }

    private static InputStream createPeekInputStream(InputStream underlying)
            throws ReflectiveOperationException {
        Class<? extends InputStream> clazz =
                Class.forName("java.io.ObjectInputStream$PeekInputStream")
                        .asSubclass(InputStream.class);

        Constructor<? extends InputStream> ctr =
                clazz.getDeclaredConstructor(InputStream.class);
        ctr.setAccessible(true);
        return ctr.newInstance(underlying);
    }

    private static void peek(InputStream pin)
            throws ReflectiveOperationException {
        Method p = pin.getClass().getDeclaredMethod("peek");
        p.setAccessible(true);
        p.invoke(pin);
    }
}
