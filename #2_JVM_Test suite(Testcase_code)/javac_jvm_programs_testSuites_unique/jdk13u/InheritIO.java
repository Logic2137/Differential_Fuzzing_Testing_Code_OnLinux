import static java.lang.ProcessBuilder.Redirect.*;

class InheritIO {

    public static class TestInheritIO {

        public static void main(String[] args) throws Throwable {
            int err = new ProcessBuilder(args).inheritIO().start().waitFor();
            System.err.print("exit value: " + err);
            System.exit(err);
        }
    }

    public static class TestRedirectInherit {

        public static void main(String[] args) throws Throwable {
            int err = new ProcessBuilder(args).redirectInput(INHERIT).redirectOutput(INHERIT).redirectError(INHERIT).start().waitFor();
            System.err.print("exit value: " + err);
            System.exit(err);
        }
    }
}
