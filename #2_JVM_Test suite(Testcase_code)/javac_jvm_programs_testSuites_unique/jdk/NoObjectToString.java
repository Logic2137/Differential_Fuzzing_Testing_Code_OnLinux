



import java.io.*;
import com.sun.tools.classfile.*;
import com.sun.tools.classfile.ConstantPool.CONSTANT_Methodref_info;

public class NoObjectToString {
    public static void main(String... args) throws Exception {
        NoObjectToString c = new NoObjectToString();
        c.run(args);
    }

    void run(String... args) throws Exception {
         
        InputStream in = NoObjectToString.class.getResourceAsStream("NoObjectToString$Test.class");
        try {
            ClassFile cf = ClassFile.read(in);
            for (ConstantPool.CPInfo cpinfo: cf.constant_pool.entries()) {
                if (cpinfo.getTag() == ConstantPool.CONSTANT_Methodref) {
                    CONSTANT_Methodref_info ref = (CONSTANT_Methodref_info) cpinfo;
                    String methodDesc = ref.getClassInfo().getName() + "." + ref.getNameAndTypeInfo().getName() + ":" + ref.getNameAndTypeInfo().getType();

                    if ("java/lang/Object.toString:()Ljava/lang/String;".equals(methodDesc)) {
                        throw new AssertionError("Found call to j.l.Object.toString");
                    }
                }
            }
        } catch (ConstantPoolException ignore) {
            throw new AssertionError(ignore);
        } finally {
            in.close();
        }
    }

    class Test {
        void test(I i, J j, K k) {
            i.toString();
            j.toString();
            k.toString();
        }
    }

    interface I {
        public String toString();
    }
    interface J extends I {}
    interface K {}

}
