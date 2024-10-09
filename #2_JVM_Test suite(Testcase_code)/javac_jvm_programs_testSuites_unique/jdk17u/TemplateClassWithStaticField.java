
package gc.g1.unloading.bytecode;

public class TemplateClassWithStaticField {

    private static Object field;

    public static void setField(Object field) {
        TemplateClassWithStaticField.field = field;
    }

    static int field2 = -1;

    public static void methodForCompilation(Object object) {
        int i = object.hashCode();
        i = i * 2000 / 1994 + 153;
        field2 = i;
    }
}
