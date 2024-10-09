



import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.ConstantPoolException;
import com.sun.tools.classfile.Method;
import com.sun.tools.classfile.Attribute;
import com.sun.tools.classfile.Code_attribute;
import com.sun.tools.classfile.LineNumberTable_attribute;
import com.sun.tools.classfile.LineNumberTable_attribute.Entry;

import java.io.File;
import java.io.IOException;

public class ConditionalLineNumberTest {
    public static void main(String[] args) throws Exception {
        
        Entry[] lines = findEntries();
        if (lines == null || lines.length != 5)
            throw new Exception("conditional line number table incorrect");

        int current = lines[0].line_number;
        for (Entry e : lines) {
            if (e.line_number != current)
                throw new Exception("conditional line number table incorrect");
            current++;
        }
   }

    static Entry[] findEntries() throws IOException, ConstantPoolException {
        ClassFile self = ClassFile.read(ConditionalLineNumberTest.class.getResourceAsStream("ConditionalLineNumberTest.class"));
        for (Method m : self.methods) {
            if ("method".equals(m.getName(self.constant_pool))) {
                Code_attribute code_attribute = (Code_attribute)m.attributes.get(Attribute.Code);
                for (Attribute at : code_attribute.attributes) {
                    if (Attribute.LineNumberTable.equals(at.getName(self.constant_pool))) {
                        return ((LineNumberTable_attribute)at).line_number_table;
                    }
                }
            }
        }
        return null;
    }

    
    
    public static String method(int field) {
        String s = field % 2 == 0 ?
            (field == 0 ? "false"
             : "true" + field) : 
            "false" + field; 
        return s;
    }
}
