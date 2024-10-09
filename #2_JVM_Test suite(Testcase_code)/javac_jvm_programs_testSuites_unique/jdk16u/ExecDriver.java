

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ExecDriver {
    
    private static final String TEST_CLASS_PATH = System.getProperty("test.class.path", ".");
    
    private static final String TEST_JDK = System.getProperty("test.jdk");
    public static void main(String[] args) throws IOException, InterruptedException {
        boolean java = false;
        boolean launcher = false;

        String type = args[0];
        switch (type) {
            case "--java":
                String[] oldArgs = args;
                int count;
                String libraryPath = System.getProperty("test.nativepath");
                if (libraryPath != null && !libraryPath.isEmpty()) {
                    count = 4;
                    args = new String[args.length + 3];
                    args[3] = "-Djava.library.path=" + libraryPath;
                } else {
                    count = 3;
                    args = new String[args.length + 2];
                }
                args[0] = javaBin();
                args[1] = "-cp";
                args[2] = TEST_CLASS_PATH;
                System.arraycopy(oldArgs, 1, args, count, oldArgs.length - 1);
                java = true;
                break;
            case "--launcher":
                java = true;
                launcher = true;
            case "--cmd":
                args = Arrays.copyOfRange(args, 1, args.length);
                break;
            default:
                throw new Error("unknown type: " + type);
        }
        
        if (java) {
            String[] oldArgs = args;
            String[] testJavaOpts = getTestJavaOpts();
            if (testJavaOpts.length > 0) {
                args = new String[args.length + testJavaOpts.length];
                
                args[0] = oldArgs[0];
                
                System.arraycopy(testJavaOpts, 0, args, 1, testJavaOpts.length);
                
                System.arraycopy(oldArgs, 1, args, 1 + testJavaOpts.length, oldArgs.length - 1);
            }
        }
        String command = Arrays.toString(args);
        System.out.println("exec " + command);

        ProcessBuilder pb = new ProcessBuilder(args);
        
        if (launcher) {
            Path dir = Paths.get(TEST_JDK);
            String value;
            String name = sharedLibraryPathVariableName();
            
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                value = dir.resolve("bin")
                           .resolve(variant())
                           .toAbsolutePath()
                           .toString();
                value += File.pathSeparator;
                value += dir.resolve("bin")
                            .toAbsolutePath()
                            .toString();
            } else {
                value = dir.resolve("lib")
                           .resolve(variant())
                           .toAbsolutePath()
                           .toString();
            }

            System.out.println("  with " + name + " = " +
                    pb.environment()
                      .merge(name, value, (x, y) -> y + File.pathSeparator + x));
            System.out.println("  with CLASSPATH = " +
                    pb.environment()
                      .put("CLASSPATH", TEST_CLASS_PATH));
        }
        Process p = pb.start();
        
        new Thread(() -> copy(p.getInputStream(), System.out)).start();
        new Thread(() -> copy(p.getErrorStream(), System.out)).start();
        int exitCode = p.waitFor();

        if (exitCode != 0 && (!java || exitCode != 95)) {
            throw new AssertionError(command + " exit code is " + exitCode);
        }
    }

    
    private static String sharedLibraryPathVariableName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("win")) {
            return "PATH";
        } else if (osName.startsWith("mac")) {
            return "DYLD_LIBRARY_PATH";
        } else if (osName.startsWith("aix")) {
            return "LIBPATH";
        } else {
            return "LD_LIBRARY_PATH";
        }
    }

    
    private static String[] getTestJavaOpts() {
        List<String> opts = new ArrayList<String>();
        {
            String v = System.getProperty("test.vm.opts", "").trim();
            if (!v.isEmpty()) {
                Collections.addAll(opts, v.split("\\s+"));
            }
        }
        {
            String v = System.getProperty("test.java.opts", "").trim();
            if (!v.isEmpty()) {
                Collections.addAll(opts, v.split("\\s+"));
            }
        }
        return opts.toArray(new String[0]);
    }

    
    private static String variant() {
        String vmName = System.getProperty("java.vm.name");
        if (vmName.endsWith(" Server VM")) {
            return "server";
        } else if (vmName.endsWith(" Client VM")) {
            return "client";
        } else if (vmName.endsWith(" Minimal VM")) {
            return "minimal";
        } else {
            throw new Error("TESTBUG: unsuppported vm variant");
        }
    }

    private static void copy(InputStream is, OutputStream os) {
        byte[] buffer = new byte[1024];
        int n;
        try (InputStream close = is) {
            while ((n = is.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String javaBin() {
        return Paths.get(TEST_JDK)
                    .resolve("bin")
                    .resolve("java")
                    .toAbsolutePath()
                    .toString();
    }
}

