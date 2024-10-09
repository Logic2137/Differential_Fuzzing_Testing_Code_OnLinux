import java.lang.invoke.*;

interface intf {

    public Object target();
}

public class Test6990212 implements intf {

    public Object target() {
        return null;
    }

    public static void main(String[] args) throws Throwable {
        MethodHandle target = MethodHandles.lookup().findVirtual(intf.class, "target", MethodType.methodType(Object.class));
        try {
            target.invoke(new Object());
        } catch (ClassCastException cce) {
            System.out.println("got expected ClassCastException");
        }
    }
}
