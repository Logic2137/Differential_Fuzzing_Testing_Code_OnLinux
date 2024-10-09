



package compiler.exceptions;

public class TestRecursiveReplacedException {

    public static void main(String args[]) {
        new TestRecursiveReplacedException().run();
    }

    public void run() {
        try {
            run();
        } catch (Throwable t) {
        }
    }
}
