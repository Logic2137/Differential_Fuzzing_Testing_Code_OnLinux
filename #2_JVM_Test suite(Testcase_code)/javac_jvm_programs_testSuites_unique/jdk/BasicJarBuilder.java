



import java.io.File;
import java.util.ArrayList;
import java.util.spi.ToolProvider;



public class BasicJarBuilder {
    private static final String classDir = System.getProperty("test.classes");

    private static final ToolProvider JAR = ToolProvider.findFirst("jar")
        .orElseThrow(() -> new RuntimeException("ToolProvider for jar not found"));

    public static void build(boolean classesInWorkDir, String jarName,
        String ...classNames) throws Exception {

        if (classesInWorkDir) {
            createSimpleJar(".", classDir + File.separator + jarName + ".jar", classNames);
        } else {
            build(jarName, classNames);
        }
    }

    public static void build(String jarName, String ...classNames) throws Exception {
        createSimpleJar(classDir, classDir + File.separator + jarName + ".jar",
            classNames);
    }

    private static void createSimpleJar(String jarclassDir, String jarName,
        String[] classNames) throws Exception {
        ArrayList<String> args = new ArrayList<String>();
        args.add("cf");
        args.add(jarName);
        addClassArgs(args, jarclassDir, classNames);
        createJar(args);
    }

    private static void addClassArgs(ArrayList<String> args, String jarclassDir,
        String[] classNames) {

        for (String name : classNames) {
            args.add("-C");
            args.add(jarclassDir);
            args.add(name + ".class");
        }
    }

    private static void createJar(ArrayList<String> args) {
        if (JAR.run(System.out, System.err, args.toArray(new String[1])) != 0) {
            throw new RuntimeException("jar operation failed");
        }
    }

    
    public static String getTestJar(String jar) {
        File dir = new File(System.getProperty("test.classes", "."));
        File jarFile = new File(dir, jar);
        if (!jarFile.exists()) {
            throw new RuntimeException("Cannot find " + jarFile.getPath());
        }
        if (!jarFile.isFile()) {
            throw new RuntimeException("Not a regular file: " + jarFile.getPath());
        }
        return jarFile.getPath();
    }
}
