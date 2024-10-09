

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.nio.file.FileVisitResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.spi.ToolProvider;

public class JPackageHelper {

    private static final boolean VERBOSE = false;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String JAVA_HOME = System.getProperty("java.home");
    public static final String TEST_SRC_ROOT;
    public static final String TEST_SRC;
    private static final Path BIN_DIR = Path.of(JAVA_HOME, "bin");
    private static final Path JPACKAGE;
    private static final Path JAVAC;
    private static final Path JAR;
    private static final Path JLINK;

    public static class ModuleArgs {
        private final String version;
        private final String mainClass;

        ModuleArgs(String version, String mainClass) {
            this.version = version;
            this.mainClass = mainClass;
        }

        public String getVersion() {
            return version;
        }

        public String getMainClass() {
            return mainClass;
        }
    }

    static {
        if (OS.startsWith("win")) {
            JPACKAGE = BIN_DIR.resolve("jpackage.exe");
            JAVAC = BIN_DIR.resolve("javac.exe");
            JAR = BIN_DIR.resolve("jar.exe");
            JLINK = BIN_DIR.resolve("jlink.exe");
        } else {
            JPACKAGE = BIN_DIR.resolve("jpackage");
            JAVAC = BIN_DIR.resolve("javac");
            JAR = BIN_DIR.resolve("jar");
            JLINK = BIN_DIR.resolve("jlink");
        }

        
        TEST_SRC = System.getProperty("test.src");
        Path root = Path.of(TEST_SRC);
        Path apps = Path.of(TEST_SRC, "apps");
        if (apps.toFile().exists()) {
            
        } else {
             apps = Path.of(TEST_SRC, "..", "apps");
             if (apps.toFile().exists()) {
                 root = apps.getParent().normalize(); 
             } else {
                 apps = Path.of(TEST_SRC, "..", "..", "apps");
                 if (apps.toFile().exists()) {
                     root = apps.getParent().normalize(); 
                 } else {
                     apps = Path.of(TEST_SRC, "..", "..", "..", "apps");
                     if (apps.toFile().exists()) {
                         root = apps.getParent().normalize(); 
                     } else {
                         
                         
                         throw new RuntimeException("we should never get here");
                     }
                 }
            }
        }
        TEST_SRC_ROOT = root.toString();
    }

    static final ToolProvider JPACKAGE_TOOL =
            ToolProvider.findFirst("jpackage").orElseThrow(
            () -> new RuntimeException("jpackage tool not found"));

    public static int execute(File out, String... command) throws Exception {
        if (VERBOSE) {
            System.out.print("Execute command: ");
            for (String c : command) {
                System.out.print(c);
                System.out.print(" ");
            }
            System.out.println();
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        if (out != null) {
            builder.redirectErrorStream(true);
            builder.redirectOutput(out);
        }

        Process process = builder.start();
        return process.waitFor();
    }

    public static Process executeNoWait(File out, String... command) throws Exception {
        if (VERBOSE) {
            System.out.print("Execute command: ");
            for (String c : command) {
                System.out.print(c);
                System.out.print(" ");
            }
            System.out.println();
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        if (out != null) {
            builder.redirectErrorStream(true);
            builder.redirectOutput(out);
        }

        return builder.start();
    }

    private static String[] getCommand(String... args) {
        String[] command;
        if (args == null) {
            command = new String[1];
        } else {
            command = new String[args.length + 1];
        }

        int index = 0;
        command[index] = JPACKAGE.toString();

        if (args != null) {
            for (String arg : args) {
                index++;
                command[index] = arg;
            }
        }

        return command;
    }

    public static void deleteRecursive(File path) throws IOException {
        if (!path.exists()) {
            return;
        }

        Path directory = path.toPath();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attr) throws IOException {
                file.toFile().setWritable(true);
                if (OS.startsWith("win")) {
                    try {
                        Files.setAttribute(file, "dos:readonly", false);
                    } catch (Exception ioe) {
                        
                        System.err.println("IOException: " + ioe);
                        ioe.printStackTrace(System.err);
                    }
                }
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir,
                    BasicFileAttributes attr) throws IOException {
                if (OS.startsWith("win")) {
                    Files.setAttribute(dir, "dos:readonly", false);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void deleteOutputFolder(String output) throws IOException {
        File outputFolder = new File(output);
        System.out.println("deleteOutputFolder: " + outputFolder.getAbsolutePath());
        try {
            deleteRecursive(outputFolder);
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe);
            ioe.printStackTrace(System.err);
            deleteRecursive(outputFolder);
        }
    }

    public static String executeCLI(boolean retValZero, String... args) throws Exception {
        int retVal;
        File outfile = new File("output.log");
        String[] command = getCommand(args);
        try {
            retVal = execute(outfile, command);
        } catch (Exception ex) {
            if (outfile.exists()) {
                System.err.println(Files.readString(outfile.toPath()));
            }
            throw ex;
        }

        String output = Files.readString(outfile.toPath());
        if (retValZero) {
            if (retVal != 0) {
                System.err.println("command run:");
                for (String s : command) { System.err.println(s); }
                System.err.println("command output:");
                System.err.println(output);
                throw new AssertionError("jpackage exited with error: " + retVal);
            }
        } else {
            if (retVal == 0) {
                System.err.println(output);
                throw new AssertionError("jpackage exited without error: " + retVal);
            }
        }

        if (VERBOSE) {
            System.out.println("output =");
            System.out.println(output);
        }

        return output;
    }

    public static String executeToolProvider(boolean retValZero, String... args) throws Exception {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        int retVal = JPACKAGE_TOOL.run(pw, pw, args);
        String output = writer.toString();

        if (retValZero) {
            if (retVal != 0) {
                System.err.println(output);
                throw new AssertionError("jpackage exited with error: " + retVal);
            }
        } else {
            if (retVal == 0) {
                System.err.println(output);
                throw new AssertionError("jpackage exited without error");
            }
        }

        if (VERBOSE) {
            System.out.println("output =");
            System.out.println(output);
        }

        return output;
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isOSX() {
        return (OS.contains("mac"));
    }

    public static boolean isLinux() {
        return ((OS.contains("nix") || OS.contains("nux")));
    }

    public static void createHelloImageJar(String inputDir) throws Exception {
        createJar(false, "Hello", "image", inputDir);
    }

    public static void createHelloImageJar() throws Exception {
        createJar(false, "Hello", "image", "input");
    }

    public static void createHelloImageJarWithMainClass() throws Exception {
        createJar(true, "Hello", "image", "input");
    }

    public static void createHelloInstallerJar() throws Exception {
        createJar(false, "Hello", "installer", "input");
    }

    public static void createHelloInstallerJarWithMainClass() throws Exception {
        createJar(true, "Hello", "installer", "input");
    }

    private static void createJar(boolean mainClassAttribute, String name,
        String testType, String inputDir) throws Exception {
        int retVal;

        File input = new File(inputDir);
        if (!input.exists()) {
            input.mkdirs();
        }

        Path src = Path.of(TEST_SRC_ROOT + File.separator + "apps"
                + File.separator + testType + File.separator + name + ".java");
        Path dst = Path.of(name + ".java");

        if (dst.toFile().exists()) {
            Files.delete(dst);
        }
        Files.copy(src, dst);


        File javacLog = new File("javac.log");
        try {
            retVal = execute(javacLog, JAVAC.toString(), name + ".java");
        } catch (Exception ex) {
            if (javacLog.exists()) {
                System.err.println(Files.readString(javacLog.toPath()));
            }
            throw ex;
        }

        if (retVal != 0) {
            if (javacLog.exists()) {
                System.err.println(Files.readString(javacLog.toPath()));
            }
            throw new AssertionError("javac exited with error: " + retVal);
        }

        File jarLog = new File("jar.log");
        try {
            List<String> args = new ArrayList<>();
            args.add(JAR.toString());
            args.add("-c");
            args.add("-v");
            args.add("-f");
            args.add(inputDir + File.separator + name.toLowerCase() + ".jar");
            if (mainClassAttribute) {
                args.add("-e");
                args.add(name);
            }
            args.add(name + ".class");
            retVal = execute(jarLog, args.stream().toArray(String[]::new));
        } catch (Exception ex) {
            if (jarLog.exists()) {
                System.err.println(Files.readString(jarLog.toPath()));
            }
            throw ex;
        }

        if (retVal != 0) {
            if (jarLog.exists()) {
                System.err.println(Files.readString(jarLog.toPath()));
            }
            throw new AssertionError("jar exited with error: " + retVal);
        }
    }

    public static void createHelloModule() throws Exception {
        createModule("Hello.java", "input", "hello", null, true);
    }

    public static void createHelloModule(ModuleArgs moduleArgs) throws Exception {
        createModule("Hello.java", "input", "hello", moduleArgs, true);
    }

    public static void createOtherModule() throws Exception {
        createModule("Other.java", "input-other", "other", null, false);
    }

    private static void createModule(String javaFile, String inputDir, String aName,
            ModuleArgs moduleArgs, boolean createModularJar) throws Exception {
        int retVal;

        File input = new File(inputDir);
        if (!input.exists()) {
            input.mkdir();
        }

        File module = new File("module" + File.separator + "com." + aName);
        if (!module.exists()) {
            module.mkdirs();
        }

        File javacLog = new File("javac.log");
        try {
            List<String> args = new ArrayList<>();
            args.add(JAVAC.toString());
            args.add("-d");
            args.add("module" + File.separator + "com." + aName);
            args.add(TEST_SRC_ROOT + File.separator + "apps" + File.separator
                    + "com." + aName + File.separator + "module-info.java");
            args.add(TEST_SRC_ROOT + File.separator + "apps"
                    + File.separator + "com." + aName + File.separator + "com"
                    + File.separator + aName + File.separator + javaFile);
            retVal = execute(javacLog, args.stream().toArray(String[]::new));
        } catch (Exception ex) {
            if (javacLog.exists()) {
                System.err.println(Files.readString(javacLog.toPath()));
            }
            throw ex;
        }

        if (retVal != 0) {
            if (javacLog.exists()) {
                System.err.println(Files.readString(javacLog.toPath()));
            }
            throw new AssertionError("javac exited with error: " + retVal);
        }

        if (createModularJar) {
            File jarLog = new File("jar.log");
            try {
                List<String> args = new ArrayList<>();
                args.add(JAR.toString());
                args.add("--create");
                args.add("--file");
                args.add(inputDir + File.separator + "com." + aName + ".jar");
                if (moduleArgs != null) {
                    if (moduleArgs.getVersion() != null) {
                        args.add("--module-version");
                        args.add(moduleArgs.getVersion());
                    }

                    if (moduleArgs.getMainClass()!= null) {
                        args.add("--main-class");
                        args.add(moduleArgs.getMainClass());
                    }
                }
                args.add("-C");
                args.add("module" + File.separator + "com." + aName);
                args.add(".");

                retVal = execute(jarLog, args.stream().toArray(String[]::new));
            } catch (Exception ex) {
                if (jarLog.exists()) {
                    System.err.println(Files.readString(jarLog.toPath()));
                }
                throw ex;
            }

            if (retVal != 0) {
                if (jarLog.exists()) {
                    System.err.println(Files.readString(jarLog.toPath()));
                }
                throw new AssertionError("jar exited with error: " + retVal);
            }
        }
    }

    public static void createRuntime() throws Exception {
        List<String> moreArgs = new ArrayList<>();
        createRuntime(moreArgs);
    }

    public static void createRuntime(List<String> moreArgs) throws Exception {
        int retVal;

        File jlinkLog = new File("jlink.log");
        try {
            List<String> args = new ArrayList<>();
            args.add(JLINK.toString());
            args.add("--output");
            args.add("runtime");
            args.add("--add-modules");
            args.add("java.base");
            args.addAll(moreArgs);

            retVal = execute(jlinkLog, args.stream().toArray(String[]::new));
        } catch (Exception ex) {
            if (jlinkLog.exists()) {
                System.err.println(Files.readString(jlinkLog.toPath()));
            }
            throw ex;
        }

        if (retVal != 0) {
            if (jlinkLog.exists()) {
                System.err.println(Files.readString(jlinkLog.toPath()));
            }
            throw new AssertionError("jlink exited with error: " + retVal);
        }
    }

    public static String listToArgumentsMap(List<String> arguments, boolean toolProvider) {
        if (arguments.isEmpty()) {
            return "";
        }

        String argsStr = "";
        for (int i = 0; i < arguments.size(); i++) {
            String arg = arguments.get(i);
            argsStr += quote(arg, toolProvider);
            if ((i + 1) != arguments.size()) {
                argsStr += " ";
            }
        }

        if (!toolProvider && isWindows()) {
            if (argsStr.contains(" ")) {
                if (argsStr.contains("\"")) {
                    argsStr = escapeQuote(argsStr, toolProvider);
                }
                argsStr = "\"" + argsStr + "\"";
            }
        }
        return argsStr;
    }

    public static String[] cmdWithAtFilename(String [] cmd, int ndx, int len)
                throws IOException {
        ArrayList<String> newAList = new ArrayList<>();
        String fileString = null;
        for (int i=0; i<cmd.length; i++) {
            if (i == ndx) {
                newAList.add("@argfile.cmds");
                fileString = cmd[i];
            } else if (i > ndx && i < ndx + len) {
                fileString += " " + cmd[i];
            } else {
                newAList.add(cmd[i]);
            }
        }
        if (fileString != null) {
            Path path = new File("argfile.cmds").toPath();
            try (BufferedWriter bw = Files.newBufferedWriter(path);
                    PrintWriter out = new PrintWriter(bw)) {
                out.println(fileString);
            }
        }
        return newAList.toArray(new String[0]);
    }

    public static String [] splitAndFilter(String output) {
        if (output == null) {
            return null;
        }

        return Stream.of(output.split("\\R"))
                .filter(str -> !str.startsWith("Picked up"))
                .filter(str -> !str.startsWith("WARNING: Using incubator"))
                .filter(str -> !str.startsWith("hello: "))
                .collect(Collectors.toList()).toArray(String[]::new);
    }

    private static String quote(String in, boolean toolProvider) {
        if (in == null) {
            return null;
        }

        if (in.isEmpty()) {
            return "";
        }

        if (!in.contains("=")) {
            
            if (in.contains(" ")) {
                in = escapeQuote(in, toolProvider);
                return "\"" + in + "\"";
            }
            return in;
        }

        if (!in.contains(" ")) {
            return in; 
        }

        int paramIndex = in.indexOf("=");
        if (paramIndex <= 0) {
            return in; 
        }

        String param = in.substring(0, paramIndex);
        String value = in.substring(paramIndex + 1);

        if (value.length() == 0) {
            return in; 
        }

        value = escapeQuote(value, toolProvider);

        return param + "=" + "\"" + value + "\"";
    }

    private static String escapeQuote(String in, boolean toolProvider) {
        if (in == null) {
            return null;
        }

        if (in.isEmpty()) {
            return "";
        }

        if (in.contains("\"")) {
            
            StringBuilder sb = new StringBuilder();
            int codeLen = in.codePointCount(0, in.length());
            for (int i = 0; i < codeLen; i++) {
                int code = in.codePointAt(i);
                
                
                
                
                
                
                
                
                
                
                
                
                
                switch (code) {
                    case '"':
                        
                        if (i == 0 || in.codePointAt(i - 1) != '\\') {
                            sb.appendCodePoint('\\');
                            sb.appendCodePoint(code);
                        }
                        break;
                    case '\\':
                        
                        if ((i + 1) < codeLen) {
                            int nextCode = in.codePointAt(i + 1);
                            if (nextCode == '"') {
                                
                                sb.appendCodePoint('\\');
                                sb.appendCodePoint('\\');
                                sb.appendCodePoint('\\');
                                sb.appendCodePoint(nextCode);
                            } else {
                                sb.appendCodePoint('\\');
                                sb.appendCodePoint(code);
                            }
                        } else {
                            sb.appendCodePoint(code);
                        }
                        break;
                    default:
                        sb.appendCodePoint(code);
                        break;
                }
            }
            return sb.toString();
        }

        return in;
    }
}
