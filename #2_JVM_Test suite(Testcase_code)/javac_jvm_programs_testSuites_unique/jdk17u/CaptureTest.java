import java.lang.Class;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ArrayList;

public class CaptureTest {

    private static final int SYNTHETIC = 0x1000;

    private static final int MANDATED = 0x8000;

    public static void main(String... args) throws Exception {
        new CaptureTest().run();
    }

    private void run() throws Exception {
        final Encloser pn = new Encloser();
        pn.makeLocal("hello").check();
        pn.makeInner("hello").check();
        pn.makeAnon("hello").check();
        pn.makeAnonExtendsLocal("hello").check();
        pn.makeAnonExtendsInner("hello").check();
        if (0 != errors)
            throw new Exception("MethodParameters test failed with " + errors + " errors");
    }

    private void error(final String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    int errors;

    abstract class Tester {

        public Tester(final int param) {
        }

        protected abstract String[] names();

        protected abstract int[] modifiers();

        protected abstract Class[] types();

        public void check() {
            final Class<?> cls = this.getClass();
            final Constructor<?> ctor = cls.getDeclaredConstructors()[0];
            final Parameter[] params = ctor.getParameters();
            final String[] names = names();
            final int[] modifiers = modifiers();
            final Class[] types = types();
            System.err.println("Testing class " + cls);
            if (params.length == names.length) {
                for (int i = 0; i < names.length; i++) {
                    System.err.println("Testing parameter " + params[i].getName());
                    if (!params[i].getName().equals(names[i]))
                        error("Expected parameter name " + names[i] + " got " + params[i].getName());
                    if (params[i].getModifiers() != modifiers[i])
                        error("Expected parameter modifiers " + modifiers[i] + " got " + params[i].getModifiers());
                    if (!params[i].getType().equals(types[i]))
                        error("Expected parameter type " + types[i] + " got " + params[i].getType());
                }
            } else
                error("Expected " + names.length + " parameters");
        }
    }

    class Encloser {

        private class InnerTester extends Tester {

            public InnerTester(final int innerparam) {
                super(innerparam);
            }

            protected String[] names() {
                return new String[] { "this$1", "innerparam" };
            }

            protected int[] modifiers() {
                return new int[] { Modifier.FINAL | SYNTHETIC, Modifier.FINAL };
            }

            protected Class[] types() {
                return new Class[] { Encloser.class, int.class };
            }
        }

        public Tester makeInner(final String message) {
            return new InnerTester(2);
        }

        public Tester makeLocal(final String message) {
            class LocalTester extends Tester {

                public LocalTester(final int localparam) {
                    super(localparam);
                }

                protected String[] names() {
                    return new String[] { "this$1", "localparam", "val$message" };
                }

                protected int[] modifiers() {
                    return new int[] { Modifier.FINAL | MANDATED, Modifier.FINAL, Modifier.FINAL | SYNTHETIC };
                }

                protected Class[] types() {
                    return new Class[] { Encloser.class, int.class, String.class };
                }

                public String message() {
                    return message;
                }
            }
            return new LocalTester(2);
        }

        public Tester makeAnonExtendsLocal(final String message) {
            abstract class LocalTester extends Tester {

                public LocalTester(final int localparam) {
                    super(localparam);
                }

                protected String[] names() {
                    return new String[] { "this$1", "localparam", "val$message" };
                }

                protected int[] modifiers() {
                    return new int[] { Modifier.FINAL | MANDATED, Modifier.FINAL, Modifier.FINAL | SYNTHETIC };
                }

                protected Class[] types() {
                    return new Class[] { Encloser.class, int.class, String.class };
                }
            }
            return new LocalTester(2) {

                public String message() {
                    return message;
                }
            };
        }

        public Tester makeAnonExtendsInner(final String message) {
            return new InnerTester(2) {

                protected String[] names() {
                    return new String[] { "this$1", "innerparam", "val$message" };
                }

                protected int[] modifiers() {
                    return new int[] { Modifier.FINAL | MANDATED, Modifier.FINAL, Modifier.FINAL | SYNTHETIC };
                }

                protected Class[] types() {
                    return new Class[] { Encloser.class, int.class, String.class };
                }

                public String message() {
                    return message;
                }
            };
        }

        public Tester makeAnon(final String message) {
            return new Tester(2) {

                protected String[] names() {
                    return new String[] { "this$1", "param", "val$message" };
                }

                protected int[] modifiers() {
                    return new int[] { Modifier.FINAL | MANDATED, Modifier.FINAL, Modifier.FINAL | SYNTHETIC };
                }

                protected Class[] types() {
                    return new Class[] { Encloser.class, int.class, String.class };
                }

                public String message() {
                    return message;
                }
            };
        }
    }
}
