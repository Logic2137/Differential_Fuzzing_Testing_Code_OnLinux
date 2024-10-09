
package compiler.loopopts;

public class TestCastIINoLoopLimitCheck {

    static void m(int i, int index, char[] buf) {
        while (i >= 65536) {
            i = i / 100;
            buf[--index] = 0;
            buf[--index] = 1;
        }
    }

    static public void main(String[] args) {
        m(0, 0, null);
    }
}
