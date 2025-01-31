



import com.sun.javadoc.*;
import java.util.*;

public class BooleanConst extends Doclet
{
    public static void main(String[] args) {
        
        if (com.sun.tools.javadoc.Main.
            execute("javadoc", "BooleanConst", BooleanConst.class.getClassLoader(),
                    new String[] {System.getProperty("test.src", ".") + java.io.File.separatorChar + "BooleanConst.java"}) != 0)
            throw new Error();
    }

    public static final boolean b1 = false;
    public static final boolean b2 = true;

    public static boolean start(com.sun.javadoc.RootDoc root) {
        ClassDoc[] classes = root.classes();
        if (classes.length != 1)
            throw new Error("1 " + Arrays.asList(classes));
        ClassDoc self = classes[0];
        FieldDoc[] fields = self.fields();
        if (fields.length != 2)
            throw new Error("2 " + Arrays.asList(fields));
        for (int i=0; i<fields.length; i++) {
            FieldDoc f = fields[i];
            if (f.name().equals("b1")) {
                Object value = f.constantValue();
                if (value == null || !(value instanceof Boolean) || ((Boolean)value).booleanValue())
                    throw new Error("4 " + value);
            } else if (f.name().equals("b2")) {
                Object value = f.constantValue();
                if (value == null || !(value instanceof Boolean) || !((Boolean)value).booleanValue())
                    throw new Error("5 " + value);
            } else throw new Error("3 " + f.name());
        }
        return true;
    }
}
