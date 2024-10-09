import java.lang.reflect.Method;

public class recordAttributeTest {

    public static void runTest(String className, String cfeMessage) {
        try {
            Class newClass = Class.forName(className);
            throw new RuntimeException("Expected ClassFormatError exception not thrown");
        } catch (java.lang.ClassFormatError e) {
            String eMsg = e.getMessage();
            if (!eMsg.contains(cfeMessage)) {
                throw new RuntimeException("Unexpected exception: " + eMsg);
            }
        } catch (java.lang.ClassNotFoundException f) {
            throw new RuntimeException("Unexpected exception: " + f.getMessage());
        }
    }

    public static void main(String... args) throws Throwable {
        runTest("twoRecordAttributes", "Multiple Record attributes in class");
        Class abstractClass = Class.forName("abstractRecord");
        Class notFinalClass = Class.forName("notFinalRecord");
        runTest("badRecordAttribute", "Invalid constant pool index 13 for descriptor in Record attribute");
        runTest("shortRecordAttribute", "Truncated class file");
        Class newClass = Class.forName("oldRecordAttribute");
        runTest("superNotJLRecord", "Truncated class file");
        Class superNoJLRClass = Class.forName("superNotJLRecordOK");
    }
}
