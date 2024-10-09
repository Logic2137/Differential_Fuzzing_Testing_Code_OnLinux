



import java.lang.invoke.*;


public class FinalVirtualCallFromInterface {

    static class Final {
        public final void fm() {}
    }

    static interface FinalUser {
        static void test() throws Throwable {
            MethodType mt = MethodType.methodType(void.class);
            MethodHandle mh = MethodHandles.lookup().findVirtual(Final.class, "fm", mt);
            Final f = new Final();
            mh.invokeExact(f);
            mh.invoke(f);
        }
    }

    public static void main(String[] args) throws Throwable {
        FinalUser.test();
    }
}
