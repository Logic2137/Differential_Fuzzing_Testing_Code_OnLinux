



import java.util.*;

public class LambdaReturnUnboxing {
    interface C {
        Character get(Character c);
    }
    static <T> T c(T t, int... i) { return t; }

    static Character d() { return List.of('d').get(0); }

    public static void main(String... args) {
        List.of('x', 'y').stream().max((a, b) -> List.of(a).get(0));
        List.of('x', 'y').stream().max((a, b) -> { return List.of(a).get(0); });

        C c = LambdaReturnUnboxing::c;
        int i = c.get('c');

        i = d();
    }
}
