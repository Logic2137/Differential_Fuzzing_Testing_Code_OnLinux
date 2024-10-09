

package gc.metaspace;

import java.util.ArrayList;


public class TestMetaspaceInitialization {
    private class Internal {
        @SuppressWarnings("unused")
        public int x;
        public Internal(int x) {
            this.x = x;
        }
    }

    private void test() {
        ArrayList<Internal> l = new ArrayList<>();
        l.add(new Internal(17));
    }

    public static void main(String[] args) {
        new TestMetaspaceInitialization().test();
    }
}
