
package gc.g1.unloading.bytecode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HumongousTemplateClassGen {

    private static final String CLASS_NAME = "HumongousTemplateClass";

    private static final String PKG_NAME = "gc.g1.unloading.bytecode";

    private static final String PKG_DIR_NAME = PKG_NAME.replace(".", File.separator);

    private static final int ITERATIONS = 1075;

    private static final double MG = (Math.pow(1024, 2));

    private static final int RECORD_COUNT = 16 * ITERATIONS + 10;

    public static void addFileTop(List<String> records) {
        records.add("package " + PKG_NAME + ";\n");
        records.add("\n");
        records.add("import java.util.*;\n");
        records.add("\n");
        records.add("public class " + CLASS_NAME + " {\n");
        records.add("    public static void main() {\n");
        records.add("        System.out.println(\"In humongous class \");\n");
        records.add("    }");
        records.add("\n");
    }

    public static void addIteration(int itNum, List<String> records) {
        records.add("    public static Object public_static_object_" + itNum + " = new Object();\n");
        records.add("    protected static Object protected_static_object_" + itNum + " = new Object();\n");
        records.add("    private static Object private_static_Object_" + itNum + " = new Object();\n");
        records.add("\n");
        records.add("    public static long public_static_long_" + itNum + ";\n");
        records.add("    protected static long protected_static_long_" + itNum + " = new Random().nextLong();\n");
        records.add("    private static long private_static_long_" + itNum + " = 42;\n");
        records.add("\n");
        records.add("    public Object public_object_" + itNum + " = new Object();\n");
        records.add("    protected Object protected_object_" + itNum + " = new Object();\n");
        records.add("    private Object private_Object_" + itNum + " = new Object();\n");
        records.add("\n");
        records.add("    public long public_long_" + itNum + " = 43;\n");
        records.add("    protected long protected_long_" + itNum + " = 44;\n");
        records.add("    private long private_long_" + itNum + " = new Random().nextLong();\n");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: HumongousTemplateClassGen " + "<vm-testbase_src_folder>");
            return;
        }
        List<String> records = new ArrayList<String>(RECORD_COUNT);
        addFileTop(records);
        for (int i = 1; i < ITERATIONS; i++) {
            addIteration(i, records);
        }
        records.add("}");
        writeBuffered(records, (int) (MG * 1), args[0]);
    }

    private static void writeBuffered(List<String> records, int bufSize, String srcDir) throws IOException {
        String path = srcDir + File.separator + PKG_DIR_NAME + File.separator + CLASS_NAME + ".java";
        System.out.println("Path=" + path);
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        long start = System.currentTimeMillis();
        FileWriter writer = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);
        for (String record : records) {
            bufferedWriter.write(record);
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000f + " seconds");
    }
}
