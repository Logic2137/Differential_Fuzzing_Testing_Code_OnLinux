import java.util.function.*;
import java.util.*;

class T8175317 {

    void m(Supplier<List<String>> s) {
    }

    void testMethodLambda(List l) {
        m(() -> l);
    }

    void testAssignLambda(List l) {
        Supplier<List<String>> s = () -> l;
    }

    void testMethodMref() {
        m(this::g);
    }

    void testAssignMref() {
        Supplier<List<String>> s = this::g;
    }

    List g() {
        return null;
    }
}
