import java.lang.reflect.Field;

public class ArrayLength {

    public static void main(String[] args) {
        int failed = 0;
        try {
            new String[0].getClass().getField("length");
            failed++;
            System.out.println("getField(\"length\") should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
        }
        try {
            new String[0].getClass().getDeclaredField("length");
            failed++;
            System.out.println("getDeclaredField(\"length\") should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
        }
        if (new String[0].getClass().getFields().length != 0) {
            failed++;
            System.out.println("getFields() for an array type should return a zero length array");
        }
        if (new String[0].getClass().getDeclaredFields().length != 0) {
            failed++;
            System.out.println("getDeclaredFields() for an array type should return a zero length array");
        }
        if (failed != 0)
            throw new RuntimeException("Test failed see log for details");
    }
}
