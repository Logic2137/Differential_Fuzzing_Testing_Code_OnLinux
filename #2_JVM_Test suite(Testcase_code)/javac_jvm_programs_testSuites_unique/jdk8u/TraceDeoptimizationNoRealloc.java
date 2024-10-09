public class TraceDeoptimizationNoRealloc {

    static void m(boolean some_condition) {
        if (some_condition) {
            return;
        }
    }

    static public void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            m(false);
        }
        m(true);
    }
}
