



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Tool;
import javax.tools.ToolProvider;

public class ReleaseOptionClashes {
    public static void main(String... args) throws Exception {
        new ReleaseOptionClashes().run();
    }

    void run() throws Exception {
        doRunTest("7", "-bootclasspath", "any");
        doRunTest("7", "-Xbootclasspath:any");
        doRunTest("7", "-Xbootclasspath/a:any");
        doRunTest("7", "-Xbootclasspath/p:any");
        doRunTest("7", "-endorseddirs", "any");
        doRunTest("7", "-extdirs", "any");
        doRunTest("7", "-source", "8");
        doRunTest("7", "-target", "8");
        doRunTest("9", "--system", "none");
        doRunTest("9", "--upgrade-module-path", "any");
    }

    void doRunTest(String release, String... args) throws Exception {
        System.out.println("Testing clashes for arguments: " + Arrays.asList(args));
        Class<?> log = Class.forName("com.sun.tools.javac.util.Log", true, cl);
        Field useRawMessages = log.getDeclaredField("useRawMessages");
        useRawMessages.setAccessible(true);
        useRawMessages.setBoolean(null, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<String> options = new ArrayList<>();
        options.addAll(Arrays.asList("--release", release));
        options.addAll(Arrays.asList(args));
        options.add(System.getProperty("test.src") + File.separator + "ReleaseOptionClashes.java");
        compiler.run(null, null, out, options.toArray(new String[0]));
        useRawMessages.setBoolean(null, false);
        if (!out.toString().equals(String.format("%s%n%s%n",
                                                 "javac: javac.err.release.bootclasspath.conflict",
                                                 "javac.msg.usage")) &&
            
            !out.toString().equals(String.format("%s%n%s%n%s%n%s%n",
                                                 "javac: javac.err.release.bootclasspath.conflict",
                                                 "javac.msg.usage",
                                                 "javac: javac.err.release.bootclasspath.conflict",
                                                 "javac.msg.usage"))) {
            throw new AssertionError(out);
        }
        System.out.println("Test PASSED.  Running javac again to see localized output:");
        compiler.run(null, null, System.out, options.toArray(new String[0]));
    }

    Tool compiler = ToolProvider.getSystemJavaCompiler();
    ClassLoader cl = compiler.getClass().getClassLoader();
}
