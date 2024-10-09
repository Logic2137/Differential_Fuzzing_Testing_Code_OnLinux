


import com.sun.tools.javac.code.Flags;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FlagsTest {
    public static void main(String[] args) throws IllegalAccessException {
        for (Field f : Flags.class.getFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            long flag = ((Number) f.get(null)).longValue();
            try {
                Flags.asFlagSet(flag);
            } catch (AssertionError e) {
                throw new AssertionError("missing Flags enum constant for: " + f.getName(), e);
            }
        }
    }
}
