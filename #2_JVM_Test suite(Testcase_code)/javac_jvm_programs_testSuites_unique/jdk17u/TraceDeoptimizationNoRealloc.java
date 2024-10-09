
package compiler.uncommontrap;

public class TraceDeoptimizationNoRealloc {

    static void m(boolean some_condition) {
        if (some_condition) {
            return;
        }
    }

    static public void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            m(false);
        }
        m(true);
    }
}
