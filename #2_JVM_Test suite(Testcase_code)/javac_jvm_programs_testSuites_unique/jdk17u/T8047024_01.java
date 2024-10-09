import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class T8047024_01 {

    @Target(ElementType.TYPE_USE)
    @interface TA {
    }

    public static void run() {
        try {
            System.out.println("");
        } catch (@TA Throwable e) {
        }
    }
}
