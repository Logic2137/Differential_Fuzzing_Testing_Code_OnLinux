import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class LUtils {

    static final com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();

    static final File cwd = new File(".").getAbsoluteFile();

    static final String JAVAHOME = System.getProperty("java.home");

    static final boolean isWindows = System.getProperty("os.name", "unknown").startsWith("Windows");

    static final File JAVA_BIN_FILE = new File(JAVAHOME, "bin");

    static final File JAVA_CMD = new File(JAVA_BIN_FILE, isWindows ? "java.exe" : "java");

    static final File JAR_BIN_FILE = new File(JAVAHOME, "bin");

    static final File JAR_CMD = new File(JAR_BIN_FILE, isWindows ? "jar.exe" : "jar");

    protected LUtils() {
    }

    public static void compile(String... args) {
        if (javac.compile(args) != 0) {
            throw new RuntimeException("compilation fails");
        }
    }

    static void createFile(File outFile, List<String> content) {
        try {
            Files.write(outFile.getAbsoluteFile().toPath(), content, Charset.defaultCharset());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static File getClassFile(File javaFile) {
        return javaFile.getName().endsWith(".java") ? new File(javaFile.getName().replace(".java", ".class")) : null;
    }

    static String getSimpleName(File inFile) {
        String fname = inFile.getName();
        return fname.substring(0, fname.indexOf("."));
    }

    static TestResult doExec(String... cmds) {
        return doExec(null, null, cmds);
    }

    static TestResult doExec(Map<String, String> envToSet, java.util.Set<String> envToRemove, String... cmds) {
        String cmdStr = "";
        for (String x : cmds) {
            cmdStr = cmdStr.concat(x + " ");
        }
        ProcessBuilder pb = new ProcessBuilder(cmds);
        Map<String, String> env = pb.environment();
        if (envToRemove != null) {
            for (String key : envToRemove) {
                env.remove(key);
            }
        }
        if (envToSet != null) {
            env.putAll(envToSet);
        }
        BufferedReader rdr = null;
        try {
            List<String> outputList = new ArrayList<>();
            pb.redirectErrorStream(true);
            Process p = pb.start();
            rdr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String in = rdr.readLine();
            while (in != null) {
                outputList.add(in);
                in = rdr.readLine();
            }
            p.waitFor();
            p.destroy();
            return new TestResult(cmdStr, p.exitValue(), outputList, env, new Throwable("current stack of the test"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    static class TestResult {

        String cmd;

        int exitValue;

        List<String> testOutput;

        Map<String, String> env;

        Throwable t;

        public TestResult(String str, int rv, List<String> oList, Map<String, String> env, Throwable t) {
            cmd = str;
            exitValue = rv;
            testOutput = oList;
            this.env = env;
            this.t = t;
        }

        void assertZero(String message) {
            if (exitValue != 0) {
                System.err.println(this);
                throw new RuntimeException(message);
            }
        }

        @Override
        public String toString() {
            StringWriter sw = new StringWriter();
            PrintWriter status = new PrintWriter(sw);
            status.println("Cmd: " + cmd);
            status.println("Return code: " + exitValue);
            status.println("Environment variable:");
            for (String x : env.keySet()) {
                status.println("\t" + x + "=" + env.get(x));
            }
            status.println("Output:");
            for (String x : testOutput) {
                status.println("\t" + x);
            }
            status.println("Exception:");
            status.println(t.getMessage());
            t.printStackTrace(status);
            return sw.getBuffer().toString();
        }
    }
}
