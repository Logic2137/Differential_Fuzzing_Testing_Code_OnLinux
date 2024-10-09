



import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class T8257740_2 {
    @Target(ElementType.TYPE_USE)
    @interface T8257740_2_Anno { }

    void test() {
        Runnable r = () -> {
            try {
                System.out.println();
            } catch (@T8257740_2_Anno Exception | Error e) { 
                e.printStackTrace();
            }
        };
    }
}
