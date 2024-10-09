



import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.Map.Entry;

import javax.tools.*;

import com.sun.source.util.*;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.comp.CompileStates.CompileState;

public class EventsBalancedTest {
    JavacTool tool = (JavacTool) ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager fm = tool.getStandardFileManager(null, null, null);

    public static void main(String... args) throws IOException {
        EventsBalancedTest t = new EventsBalancedTest();
        try {
            t.test();
        } finally {
            t.fm.close();
        }
    }

    void test() throws IOException {
        TestSource a = new TestSource("B", "class B extends A { }");
        TestSource b = new TestSource("A", "abstract class A { }");

        test(null, Arrays.asList(a, b));
        test(null, Arrays.asList(b, a));

        for (CompileState stop : CompileState.values()) {
            test(Arrays.asList("--should-stop:ifNoError=" + stop,
                               "--should-stop:ifError=" + stop),
                 Arrays.asList(a, b));
            test(Arrays.asList("--should-stop:ifNoError=" + stop,
                               "--should-stop:ifError=" + stop),
                 Arrays.asList(b, a));
        }
    }

    void test(List<String> options, List<JavaFileObject> files) throws IOException {
        System.err.println("testing: " + options + ", " + files);
        TestListener listener = new TestListener();
        JavacTask task = tool.getTask(null, fm, null, options, null, files);

        task.setTaskListener(listener);

        task.call();

        for (Entry<Kind, Integer> e : listener.kind2Count.entrySet()) {
            if (e.getValue() != null && e.getValue() != 0) {
                throw new IllegalStateException("Not balanced event: " + e.getKey());
            }
        }
    }

    static class TestListener implements TaskListener {
        final Map<Kind, Integer> kind2Count = new HashMap<>();

        int get(Kind k) {
            Integer count = kind2Count.get(k);

            if (count == null)
                kind2Count.put(k, count = 0);

            return count;
        }

        @Override
        public void started(TaskEvent e) {
            kind2Count.put(e.getKind(), get(e.getKind()) + 1);
        }

        @Override
        public void finished(TaskEvent e) {
            int count = get(e.getKind());

            if (count <= 0)
                throw new IllegalStateException("count<=0 for: " + e.getKind());

            kind2Count.put(e.getKind(), count - 1);
        }

    }
    static class TestSource extends SimpleJavaFileObject {
        final String content;
        public TestSource(String fileName, String content) {
            super(URI.create("myfo:/" + fileName + ".java"), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

}
