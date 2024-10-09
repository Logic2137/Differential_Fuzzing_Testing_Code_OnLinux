



import java.io.*;
import java.util.*;
import java.lang.StringBuilder;

public class AccessModifiers {
    public int errorCount;
    protected String protectedField;
    String packageField;
    private String privateField;

    public static void main(String[] args) throws Exception {
        new AccessModifiers().run();
    }

    private void run() throws Exception {
        List<String> pubMembers = new ArrayList<String>();
        pubMembers.add("public int errorCount");
        pubMembers.add("public AccessModifiers");
        pubMembers.add("public static void main");

        List<String> proMembers = new ArrayList<String>();
        proMembers.add("protected java.lang.String protectedField");
        proMembers.add("protected java.lang.String runJavap");

        List<String> pkgMembers = new ArrayList<String>();
        pkgMembers.add("java.lang.String packageField");
        pkgMembers.add("boolean verify");
        pkgMembers.add("void error");

        List<String> priMembers = new ArrayList<String>();
        priMembers.add("private java.lang.String privateField");
        priMembers.add("private void run() throws java.lang.Exception");
        priMembers.add("private void test");

        List<String> expectedList = new ArrayList<String>();

        expectedList.addAll(pubMembers);
        test("-public", expectedList);

        expectedList.addAll(proMembers);
        test("-protected", expectedList);

        expectedList.addAll(pkgMembers);
        test("-package", expectedList);

        expectedList.addAll(priMembers);
        test("-private", expectedList);

        if (errorCount > 0)
            throw new Exception(errorCount + " errors received");
    }

    private void test(String option, List<String> expectedStrs) throws Exception {
        String output = runJavap(0, option);
        if (verify(output, expectedStrs))
            System.out.println(option + " test passed");
    }

    protected String runJavap(int expect, String... options) {
        
        List<String> optlist = new ArrayList<String>();
        optlist.addAll(Arrays.asList(options));
        optlist.add("AccessModifiers");
        String[] newoptions = optlist.toArray(new String[optlist.size()]);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        System.out.printf("\nRun javap " + optlist + "\n\n");
        int rc = com.sun.tools.javap.Main.run(newoptions, pw);
        pw.close();
        System.out.println(sw);
        if (rc != expect)
           throw new Error("Expect to return " + expect + ", but return " + rc);
        return sw.toString();
    }

    boolean verify(String output, List<String> expects) {
        boolean pass = true;
        for (String expect: expects) {
            if (!output.contains(expect)) {
                error(expect + " not found");
                pass = false;
            }
        }
        return pass;
    }

    void error(String msg) {
        System.err.println(msg);
        errorCount++;
    }
}
