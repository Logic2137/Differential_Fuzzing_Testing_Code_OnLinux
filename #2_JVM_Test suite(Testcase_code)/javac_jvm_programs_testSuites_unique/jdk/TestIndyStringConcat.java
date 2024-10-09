

import com.sun.tools.classfile.*;
import com.sun.tools.classfile.BootstrapMethods_attribute.BootstrapMethodSpecifier;
import com.sun.tools.classfile.ConstantPool.CONSTANT_InvokeDynamic_info;
import com.sun.tools.classfile.ConstantPool.CONSTANT_MethodHandle_info;

import java.io.File;


public class TestIndyStringConcat {

    static String other;

    public static String test() {
        return "Foo" + other;
    }

    public static void main(String[] args) throws Exception {
        boolean expected = Boolean.valueOf(args[0]);
        boolean actual = hasStringConcatFactoryCall("test");
        if (expected != actual) {
            throw new AssertionError("expected = " + expected + ", actual = " + actual);
        }
    }

    public static boolean hasStringConcatFactoryCall(String methodName) throws Exception {
        ClassFile classFile = ClassFile.read(new File(System.getProperty("test.classes", "."),
                TestIndyStringConcat.class.getName() + ".class"));
        ConstantPool constantPool = classFile.constant_pool;

        BootstrapMethods_attribute bsm_attr =
                (BootstrapMethods_attribute)classFile
                        .getAttribute(Attribute.BootstrapMethods);

        for (Method method : classFile.methods) {
            if (method.getName(constantPool).equals(methodName)) {
                Code_attribute code = (Code_attribute) method.attributes
                        .get(Attribute.Code);
                for (Instruction i : code.getInstructions()) {
                    if (i.getOpcode() == Opcode.INVOKEDYNAMIC) {
                        CONSTANT_InvokeDynamic_info indyInfo =
                                (CONSTANT_InvokeDynamic_info) constantPool.get(i.getUnsignedShort(1));

                        BootstrapMethodSpecifier bsmSpec =
                                bsm_attr.bootstrap_method_specifiers[indyInfo.bootstrap_method_attr_index];

                        CONSTANT_MethodHandle_info bsmInfo =
                                (CONSTANT_MethodHandle_info) constantPool.get(bsmSpec.bootstrap_method_ref);

                        if (bsmInfo.getCPRefInfo().getClassName().equals("java/lang/invoke/StringConcatFactory")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
