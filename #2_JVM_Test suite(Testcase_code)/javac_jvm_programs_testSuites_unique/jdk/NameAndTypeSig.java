





public class NameAndTypeSig {
    public static void main(String args[]) throws Throwable {

        
        
        Class newClass = Class.forName("nonVoidInitSig");

        
        
        
        try {
            Class newClass2 = Class.forName("nonVoidInitSigCFE");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Method \"<init>\" in class nonVoidInitSigCFE has illegal signature")) {
                throw new RuntimeException("Wrong ClassFormatError exception: " + e.getMessage());
            }
        }

        
        
        
        try {
            Class newClass2 = Class.forName("voidInitBadSig");
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            if (!e.getMessage().contains("Method \"<init>\" in class voidInitBadSig has illegal signature \"()))V\"")) {
                throw new RuntimeException("Wrong ClassFormatError exception: " + e.getMessage());
            }
        }
        System.out.println("Test NameAndTypeSig passed.");
    }
}
