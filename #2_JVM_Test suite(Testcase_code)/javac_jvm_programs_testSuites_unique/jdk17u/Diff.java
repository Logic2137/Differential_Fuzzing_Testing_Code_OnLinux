
package jdk.test.lib.format;

public interface Diff {

    public static class Defaults {

        private Defaults() {
        }

        public final static int WIDTH = 80;

        public final static int CONTEXT_BEFORE = 2;
    }

    String format();

    boolean areEqual();
}
