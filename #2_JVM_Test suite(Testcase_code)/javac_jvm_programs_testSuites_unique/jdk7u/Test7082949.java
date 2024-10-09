



import java.lang.invoke.*;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

public class Test7082949 implements Runnable {
    public static void main(String... args) throws Throwable {
        new Thread(new Test7082949()).start();
    }

    public static Test7082949 test() {
        return null;
    }

    public void run() {
        try {
            MethodHandle m1 = MethodHandles.lookup().findStatic(Test7082949.class, "test",  methodType(Test7082949.class));
            Test7082949 v = (Test7082949)m1.invokeExact();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
