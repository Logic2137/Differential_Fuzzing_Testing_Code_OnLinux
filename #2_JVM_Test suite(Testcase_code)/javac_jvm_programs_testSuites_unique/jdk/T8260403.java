


import java.io.PrintWriter;

public class T8260403 {

    public static void main(String args[]) throws Exception {
        if (com.sun.tools.javap.Main.run(new String[]{"-c", System.getProperty("test.classes") + "/InvalidSignature.class"},
                new PrintWriter(System.out)) != 0) {
            throw new AssertionError();
        }
    }
}
