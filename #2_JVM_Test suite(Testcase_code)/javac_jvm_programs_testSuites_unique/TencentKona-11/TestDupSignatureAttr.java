



public class TestDupSignatureAttr {
    public static void main(String args[]) throws Throwable {

        System.out.println("Regression test for bug 8176147");

        String[] badClasses = new String[] {
            "DupClassSigAttrs",
            "DupMthSigAttrs",
            "DupFldSigAttrs",
        };
        String[] messages = new String[] {
            "Multiple Signature attributes in class file",
            "Multiple Signature attributes for method",
            "Multiple Signature attributes for field",
        };

        for (int x = 0; x < badClasses.length; x++) {
            try {
                Class newClass = Class.forName(badClasses[x]);
                throw new RuntimeException("Expected ClassFormatError exception not thrown");
            } catch (java.lang.ClassFormatError e) {
                if (!e.getMessage().contains(messages[x])) {
                    throw new RuntimeException("Wrong ClassFormatError exception thrown: " +
                                               e.getMessage());
                }
            }
        }

        
        Class newClass = Class.forName("OkaySigAttrs");
    }
}
