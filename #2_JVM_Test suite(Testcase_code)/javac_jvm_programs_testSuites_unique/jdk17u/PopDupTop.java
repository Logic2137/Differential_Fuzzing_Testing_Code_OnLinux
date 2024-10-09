public class PopDupTop {

    public static void testClass(String class_name, String msg) throws Throwable {
        try {
            Class newClass = Class.forName(class_name);
            throw new RuntimeException("Expected VerifyError exception not thrown for " + msg);
        } catch (java.lang.VerifyError e) {
            if (!e.getMessage().contains("Bad type on operand stack")) {
                throw new RuntimeException("Unexpected VerifyError message for " + msg + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Regression test for bug 8149607");
        testClass("dup_x1", "dup_x1 of long,ref");
        testClass("dup2toptop", "dup2 of top,top");
        testClass("dup2longtop", "dup2 of long,top");
        testClass("dup2_x1", "dup2_x1 long,ref,ref");
        testClass("dup2_x2", "dup2_x2 top");
        testClass("dup2_x2_long_refs", "dup2_x2 long,ref,ref,ref");
        testClass("poptop", "pop of top");
        testClass("poptoptop", "pop of top,top");
        testClass("pop2toptop", "pop2 of top,top");
        testClass("pop2longtop", "pop2 of long,top");
        testClass("swaptoptop", "swap of top,top");
        testClass("swapinttop", "swap of int,top");
        testClass("swaptopint", "swap of top,int");
    }
}
