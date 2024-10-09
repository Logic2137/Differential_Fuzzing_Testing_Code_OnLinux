



import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.tools.*;

import com.sun.source.util.JavacTask;

public class ExpressionSwitchToString {

    private static final String CODE =
            "public class C {" +
            "    void t1(Integer i) {" +
            "        switch (i) {" +
            "            case null: i++; break;" +
            "            case 0, 1: i++; break;" +
            "            default: i++; break;" +
            "        }" +
            "    }" +
            "    int t2(Integer i) {" +
            "        return switch (i) {" +
            "            case null: yield 0;" +
            "            case 0, 1: yield 1;" +
            "            default: yield 2;" +
            "        }" +
            "    }" +
            "}";

    private static final String EXPECTED =
            "\n" +
            "public class C {\n" +
            "    \n" +
            "    void t1(Integer i) {\n" +
            "        switch (i) {\n" +
            "        case null:\n" +
            "            i++;\n" +
            "            break;\n" +
            "        \n" +
            "        case 0, 1:\n" +
            "            i++;\n" +
            "            break;\n" +
            "        \n" +
            "        default:\n" +
            "            i++;\n" +
            "            break;\n" +
            "        \n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    int t2(Integer i) {\n" +
            "        return switch (i) {\n" +
            "        case null:\n" +
            "            yield 0;\n" +
            "        \n" +
            "        case 0, 1:\n" +
            "            yield 1;\n" +
            "        \n" +
            "        default:\n" +
            "            yield 2;\n" +
            "        \n" +
            "        };\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) throws Exception {
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        assert tool != null;
        DiagnosticListener<JavaFileObject> noErrors = d -> {};
        String sourceVersion = Integer.toString(Runtime.version().feature());

        JavacTask ct = (JavacTask) tool.getTask(null, null, noErrors,
            List.of("-XDdev"), null,
            Arrays.asList(new MyFileObject(CODE)));
        String actualCode = ct.parse().iterator().next().toString();
        actualCode = actualCode.replace(System.getProperty("line.separator"), "\n");
        if (!EXPECTED.equals(actualCode)) {
            throw new AssertionError("Unexpected toString outcome: " +
                                     actualCode);
        }
    }

    static class MyFileObject extends SimpleJavaFileObject {
        private String text;

        public MyFileObject(String text) {
            super(URI.create("myfo:/Test.java"), JavaFileObject.Kind.SOURCE);
            this.text = text;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return text;
        }
    }
}
