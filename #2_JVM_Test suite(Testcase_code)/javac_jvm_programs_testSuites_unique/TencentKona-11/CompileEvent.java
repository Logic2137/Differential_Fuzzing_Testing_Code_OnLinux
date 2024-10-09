import java.io.*;
import java.util.*;
import javax.tools.*;
import com.sun.source.util.*;
import com.sun.tools.javac.Main;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Log.WriterKind;

public class CompileEvent {

    public static void main(String... args) throws IOException {
        new CompileEvent().run();
    }

    void run() throws IOException {
        String testClasses = System.getProperty("test.classes");
        File pluginRegistration = new File(testClasses + "/META-INF/services/com.sun.source.util.Plugin");
        pluginRegistration.getParentFile().mkdirs();
        try (Writer metaInfRegistration = new FileWriter(pluginRegistration)) {
            metaInfRegistration.write("CompileEvent$PluginImpl");
        }
        File test = new File(testClasses + "/Test.java");
        test.getParentFile().mkdirs();
        try (Writer testFileWriter = new FileWriter(test)) {
            testFileWriter.write("public class Test { }");
        }
        StringWriter out;
        out = new StringWriter();
        int mainResult = Main.compile(new String[] { "-XDaccessInternalAPI", "-Xplugin:compile-event", "-processorpath", testClasses, test.getAbsolutePath() }, new PrintWriter(out, true));
        if (mainResult != 0)
            throw new AssertionError("Compilation failed unexpectedly, exit code: " + mainResult);
        assertOutput(out.toString());
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fm = comp.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> testFileObjects = fm.getJavaFileObjects(test);
            List<String> options = Arrays.asList("-XDaccessInternalAPI", "-Xplugin:compile-event", "-processorpath", testClasses);
            out = new StringWriter();
            boolean compResult = comp.getTask(out, null, null, options, null, testFileObjects).call();
            if (!compResult)
                throw new AssertionError("Compilation failed unexpectedly.");
            assertOutput(out.toString());
        }
    }

    void assertOutput(String found) {
        String lineSeparator = System.getProperty("line.separator");
        if (!found.trim().replace(lineSeparator, "\n").equals(EXPECTED)) {
            System.err.println("Expected: " + EXPECTED);
            System.err.println("Found:    " + found);
            throw new AssertionError("Unexpected events: " + found);
        }
    }

    private static final String EXPECTED = "started(COMPILATION)\n" + "started(PARSE:Test.java)\n" + "finished(PARSE:Test.java)\n" + "started(ENTER:Test.java)\n" + "finished(ENTER:Test.java)\n" + "started(ANALYZE:Test.java:Test)\n" + "finished(ANALYZE:Test.java:Test)\n" + "started(GENERATE:Test.java:Test)\n" + "finished(GENERATE:Test.java:Test)\n" + "finished(COMPILATION)";

    private static class TaskListenerImpl implements TaskListener {

        private final PrintWriter out;

        public TaskListenerImpl(PrintWriter out) {
            this.out = out;
        }

        @Override
        public void started(TaskEvent e) {
            dumpTaskEvent("started", e);
        }

        @Override
        public void finished(TaskEvent e) {
            dumpTaskEvent("finished", e);
        }

        private void dumpTaskEvent(String type, TaskEvent e) {
            StringBuilder data = new StringBuilder();
            data.append(type);
            data.append("(");
            data.append(e.getKind());
            if (e.getSourceFile() != null) {
                data.append(":");
                data.append(new File(e.getSourceFile().getName()).getName());
            }
            if (e.getTypeElement() != null) {
                data.append(":");
                data.append(e.getTypeElement().getQualifiedName());
            }
            data.append(")");
            out.println(data);
        }
    }

    public static final class PluginImpl implements Plugin {

        @Override
        public String getName() {
            return "compile-event";
        }

        @Override
        public void init(JavacTask task, String... args) {
            Context context = ((BasicJavacTask) task).getContext();
            Log log = Log.instance(context);
            task.addTaskListener(new TaskListenerImpl(log.getWriter(WriterKind.NOTICE)));
        }
    }
}
