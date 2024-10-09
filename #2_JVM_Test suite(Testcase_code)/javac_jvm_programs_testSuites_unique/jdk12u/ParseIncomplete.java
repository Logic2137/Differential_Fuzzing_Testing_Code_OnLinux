



import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.tools.*;

import com.sun.source.util.JavacTask;

public class ParseIncomplete {

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
            "            case null: break 0;" +
            "            case 0, 1: break 1;" +
            "            default: break 2;" +
            "        }" +
            "    }" +
            "}";

    public static void main(String[] args) throws Exception {
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        assert tool != null;
        DiagnosticListener<JavaFileObject> noErrors = d -> {};

        for (int i = 0; i < CODE.length(); i++) {
            String code = CODE.substring(0, i + 1);
            StringWriter out = new StringWriter();
            try {
                JavacTask ct = (JavacTask) tool.getTask(out, null, noErrors,
                    List.of("-XDdev", "--enable-preview", "-source", "12"), null,
                    Arrays.asList(new MyFileObject(code)));
                ct.parse().iterator().next();
            } catch (Throwable t) {
                System.err.println("Unexpected exception for code: " + code);
                System.err.println("output: " + out);
                throw t;
            }
            if (!out.toString().isEmpty()) {
                System.err.println("Unexpected compiler for code: " + code);
                System.err.println(out);
                throw new AssertionError();
            }
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
