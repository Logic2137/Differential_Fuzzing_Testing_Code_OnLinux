

import java.util.function.Consumer;
import java.nio.ByteBuffer;


class T8154180a {
    T8154180a(Consumer<ByteBuffer> cb) { }

    public static void main(String[] args) {
        new T8154180a(b -> System.out.println(asString(b)));
        new T8154180a((b -> System.out.println(asString(b))));
        new T8154180a(true ? b -> System.out.println(asString(b)) : b -> System.out.println(asString(b)));
        new T8154180a((true ? b -> System.out.println(asString(b)) : b -> System.out.println(asString(b))));
        new T8154180a((true ? (b -> System.out.println(asString(b))) : (b -> System.out.println(asString(b)))));
    }

    static String asString(ByteBuffer buf) {
        return null;
    }
}
