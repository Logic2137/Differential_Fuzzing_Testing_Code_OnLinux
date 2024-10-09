



package compiler.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Supplier;

public class TestMethodHandleSpeculation {

    public static void main(String... args) {
        byte[] serObj = {1};
        MyClass<byte[]> obj = new MyClass<>();
        for (int i = 0; i < 100_000; i++) {
            boolean test = obj.test(serObj);
            if (test) {
                throw new IllegalStateException("Cannot be null");
            }
        }
    }

    static class MyClass<V extends Serializable> {
        boolean test(V obj) {
            Supplier<Boolean> supp = () -> (obj == null);
            return supp.get();
        }
    }

}
