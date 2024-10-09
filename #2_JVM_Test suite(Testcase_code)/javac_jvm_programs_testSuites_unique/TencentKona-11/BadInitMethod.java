





public class BadInitMethod {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8130669");
        try {
            Class newClass = Class.forName("nonvoidClinit");
            throw new RuntimeException(
                "Expected ClassFormatError exception for non-void <clinit> not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadInitMethod passed for non-void <clinit>");
        }

        try {
            Class newClass = Class.forName("clinitNonStatic");
            throw new RuntimeException(
                "Expected ClassFormatError exception for non-static <clinit> not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadInitMethod passed for non-static <clinit>");
        }

        
        try {
            Class newClass = Class.forName("clinitArg");
        } catch (java.lang.ClassFormatError e) {
            throw new RuntimeException(
                "Unexpected ClassFormatError exception for <clinit> with argument in class file < 51");
        }

        
        try {
            Class newClass = Class.forName("clinitArg51");
            throw new RuntimeException(
                "Expected ClassFormatError exception for <clinit> with argument not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadInitMethod passed for <clinit> with argument");
        }

        try {
            Class newClass = Class.forName("badInit");
            throw new RuntimeException(
                "Expected ClassFormatError exception for non-void <init> not thrown");
        } catch (java.lang.ClassFormatError e) {
            System.out.println("Test BadInitMethod passed for non-void <init>");
        }
    }
}
