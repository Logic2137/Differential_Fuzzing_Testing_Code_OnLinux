



import java.util.function.Supplier;

public class VerifierErrorInnerPlusLambda {
    public static void main(String[] args) {
        Object a = new Object();
        class Local { Object ref = a; }
        new Object() {
            void unused() {
                Supplier<Local> s = () -> new Local();
            }
        };
    }
}
