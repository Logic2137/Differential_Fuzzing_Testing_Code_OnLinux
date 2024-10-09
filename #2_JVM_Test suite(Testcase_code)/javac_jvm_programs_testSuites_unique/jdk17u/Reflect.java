import java.util.logging.LogManager;
import java.lang.reflect.Method;

public class Reflect {

    static void printMethods(Class<?> c) {
        System.out.println(c);
        for (Method m : c.getDeclaredMethods()) {
            System.out.println("    " + m);
        }
    }

    public static void main(String[] args) {
        printMethods(java.util.logging.LogManager.class);
        printMethods(java.util.logging.LogManager.getLogManager().getClass());
    }
}
