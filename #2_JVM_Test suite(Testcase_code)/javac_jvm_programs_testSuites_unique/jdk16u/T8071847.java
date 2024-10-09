




import java.io.*;
import java.util.*;

public class T8071847 {
    String testclass="invalidFlags.class";
    String testclassHexString =
    "CAFEBABE00000031000D0A0003000A07000B07000C0100063C696E69743E0100" +
    "03282956010004436F646501000F4C696E654E756D6265725461626C6501000A" +
    "536F7572636546696C65010009546573742E6A6176610C0004000501000C696E" +
    "76616C6964466C6167730100106A6176612F6C616E672F4F626A656374002000" +
    "02000300000000000100A000040005000100060000001D00010001000000052A" +
    "B70001B10000000100070000000600010000000100010008000000020009";

    String testJavaFile = "testInvalidFlags.java";
    String testJavaSource ="public class testInvalidFlags extends invalidFlags {" +
        "invalidFlags c = null;" +
        "public testInvalidFlags() {  c = new invalidFlags(); }" +
        "public static void main(String... args) { " +
        "new testInvalidFlags();}}";

    public static void main(String[] args) throws Exception {
        new  T8071847().run();
    }

    public void run() throws IOException {
        writeHexFile(testclass,testclassHexString);
        writeTestFile(testJavaFile, testJavaSource);
        javac(testJavaFile);
    }

    File writeTestFile(String fname, String source) throws IOException {
        File f = new File(fname);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        out.println(source);
        out.close();
        return f;
    }

    byte[] hexToByte(String str) {
        char[] CA = str.toCharArray();
        byte[] byteArry = new byte[str.length()/2];
        int bi = 0;
        for (int i = 0; i<CA.length ; i+=2) {
            char c1 = CA[i], c2=CA[i+1];
            byteArry[bi++] = (byte)((Character.digit((int)c1,16)<<4) +
                             Character.digit((int)c2,16));
        }
        return byteArry;
    }

    File writeHexFile(String classFileName, String hexString) throws IOException {
        File f = new File(classFileName);
        FileOutputStream output  = new FileOutputStream(f);
        output.write(hexToByte(hexString));
        output.close();
        return f;
    }

    String javac(String className) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        int rc = 0;
        List<String> javacArgs = new ArrayList<>();
        javacArgs.addAll(Arrays.asList("-XDrawDiagnostics", "-cp", ".", "-d", ".", className));
        rc = com.sun.tools.javac.Main.compile(
            javacArgs.toArray(new String[javacArgs.size()]),out);
        out.close();
        if (rc > 1) {
            System.out.println(sw.toString());
            throw new Error("javac " + className + " failed. rc=" + rc);
        }
        if (rc != 1 || !sw.toString().contains("compiler.misc.malformed.vararg.method"))
            throw new RuntimeException("Unexpected output" + sw.toString());
        return sw.toString();
    }
}
