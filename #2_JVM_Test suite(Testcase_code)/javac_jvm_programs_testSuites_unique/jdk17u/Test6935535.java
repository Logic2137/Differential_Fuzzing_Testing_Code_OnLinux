
package compiler.codegen;

public class Test6935535 {

    static int IndexOfTest(String str) {
        return str.indexOf("1111111111111xx1x");
    }

    public static void main(String[] args) {
        String str = "1111111111111xx1111111111111xx1x";
        str = str.substring(0, 31);
        int idx = IndexOfTest(str);
        System.out.println("IndexOf(" + "1111111111111xx1x" + ") = " + idx + " in " + str);
        if (idx != -1) {
            System.exit(97);
        }
    }
}
