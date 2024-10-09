




public class InitInInterface {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8130183");
        try {
            Class newClass = Class.forName("nonvoidinit");
            throw new RuntimeException(
                 "ClassFormatError not thrown for non-void <init> in an interface");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Interface cannot have a method named <init>")) {
                throw new RuntimeException("Unexpected exception nonvoidint: " + e.getMessage());
            }
        }
        try {
            Class newClass = Class.forName("voidinit");
            throw new RuntimeException(
                 "ClassFormatError not thrown for void <init> in an interface");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Interface cannot have a method named <init>")) {
                throw new RuntimeException("Unexpected exception voidint: " + e.getMessage());
            }
        }
    }
}
