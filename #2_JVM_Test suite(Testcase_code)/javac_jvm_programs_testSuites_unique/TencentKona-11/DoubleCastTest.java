import java.util.List;
import java.util.ArrayList;
import com.sun.tools.classfile.*;
import com.sun.tools.javac.util.Assert;

public class DoubleCastTest {

    class C {

        Object x;

        Object m() {
            return null;
        }

        void m1(byte[] b) {
        }

        void m2() {
            Object o;
            Object[] os = null;
            m1((byte[]) (o = null));
            m1((byte[]) o);
            m1((byte[]) (o == null ? o : o));
            m1((byte[]) m());
            m1((byte[]) os[0]);
            m1((byte[]) this.x);
            m1((byte[]) ((byte[]) (o = null)));
        }
    }

    public static void main(String... cmdline) throws Exception {
        ClassFile cls = ClassFile.read(DoubleCastTest.class.getResourceAsStream("DoubleCastTest$C.class"));
        for (Method m : cls.methods) check(m);
    }

    static void check(Method m) throws Exception {
        boolean last_is_cast = false;
        int last_ref = 0;
        Code_attribute ea = (Code_attribute) m.attributes.get(Attribute.Code);
        for (Instruction i : ea.getInstructions()) {
            if (i.getOpcode() == Opcode.CHECKCAST) {
                Assert.check(!(last_is_cast && last_ref == i.getUnsignedShort(1)), "Double cast found - Test failed");
                last_is_cast = true;
                last_ref = i.getUnsignedShort(1);
            } else {
                last_is_cast = false;
            }
        }
    }
}
