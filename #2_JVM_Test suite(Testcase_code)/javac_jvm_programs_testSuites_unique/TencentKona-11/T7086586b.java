


import java.util.List;

public class T7086586b {

    int assertionCount = 0;

    void assertTrue(boolean cond) {
        if (!cond) {
            throw new AssertionError();
        }
        assertionCount++;
    }

    <T> void m(List<? super T> dummy) { assertTrue(true); }
    <T> void m(Object dummy) { assertTrue(false); }

    void test(List<?> l) {
        m(l);
        assertTrue(assertionCount == 1);
    }

    public static void main(String[] args) {
        new T7086586b().test(null);
    }
}
