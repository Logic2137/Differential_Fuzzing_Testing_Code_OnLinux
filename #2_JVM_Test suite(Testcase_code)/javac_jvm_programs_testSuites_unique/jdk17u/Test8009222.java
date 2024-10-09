import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

interface Intf {

    static int i = 0;
}

public class Test8009222 {

    public static void main(String[] args) throws Exception {
        Objects.requireNonNull(MethodHandles.lookup().findStaticGetter(Intf.class, "i", int.class));
        System.out.println("TEST PASSED");
    }
}
