
package metaspace.stressHierarchy.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class HumongousClassGen {

private static final String CLASS_NAME = "HumongousClass";
private static final String PKG_NAME = "metaspace.stressHierarchy.common";
private static final String PKG_DIR_NAME = PKG_NAME.replace(".",
        File.separator);
private static final int ITERATIONS = 65300;
private static final double MG = (Math.pow(1024, 2));
private static final int RECORD_COUNT = ITERATIONS + 10;

public static void addFileHeader(List<String> records) {
    records.add("package " + PKG_NAME + ";\n");
    records.add("\n");
    records.add("public class " + CLASS_NAME + " {\n");
    records.add("\n");
}

public static void main(String[] args) throws Exception {
    if (args.length < 1) {
        System.out.println("Usage: HumongousClassGen "
                        + "<vm-testbase_src_folder>");
        throw new RuntimeException("Can't generate " + PKG_NAME + "." + CLASS_NAME);
    }

    List<String> records = new ArrayList<String>(RECORD_COUNT);
    addFileHeader(records);
    for (int i = 1; i <= ITERATIONS; i++) {
        records.add("public long long" + i + ";\n");
    }
    records.add("}");
    writeBuffered(records, (int) (MG * 1), args[0]);
}

private static void writeBuffered(List<String> records, int bufSize,
        String srcDir) throws IOException {
    String path = srcDir + File.separator + PKG_DIR_NAME + File.separator
            + CLASS_NAME + ".java";
    System.out.println("Path="+path);
    File file = new File (path);
    file.getParentFile().mkdirs();
    file.createNewFile();
    long start = System.currentTimeMillis();
    FileWriter writer = new FileWriter(file);
    BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

    for (String record: records) {
       bufferedWriter.write(record);
    }
    bufferedWriter.flush();
    bufferedWriter.close();
    long end = System.currentTimeMillis();
    System.out.println((end - start) / 1000f + " seconds");
}
}
