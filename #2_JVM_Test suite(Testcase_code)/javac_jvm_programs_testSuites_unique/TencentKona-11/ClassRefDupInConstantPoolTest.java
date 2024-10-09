



import java.util.TreeSet;

import com.sun.tools.classfile.*;
import com.sun.tools.classfile.ConstantPool.*;

public class ClassRefDupInConstantPoolTest {
    public static void main(String[] args) throws Exception {
        ClassFile cls = ClassFile.read(ClassRefDupInConstantPoolTest.class.
                                       getResourceAsStream("ClassRefDupInConstantPoolTest$Duplicates.class"));
        ConstantPool pool = cls.constant_pool;

        int duplicates = 0;
        TreeSet<Integer> set = new TreeSet<>();
        for (CPInfo i : pool.entries()) {
            if (i.getTag() == ConstantPool.CONSTANT_Class) {
                CONSTANT_Class_info ci = (CONSTANT_Class_info)i;
                if (!set.add(ci.name_index)) {
                    duplicates++;
                    System.out.println("DUPLICATE CLASS REF " + ci.getName());
                }
            }
        }
        if (duplicates > 0)
            throw new Exception("Test Failed");
    }

    class Duplicates {
        String concat(String s1, String s2) {
            return s1 + (s2 == s1 ? " " : s2);
        }
    }
}
