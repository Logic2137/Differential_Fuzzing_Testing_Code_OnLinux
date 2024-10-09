



package test.java.lang.invoke;

import java.util.*;
import java.io.*;
import java.lang.invoke.*;
import java.security.*;

import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodType.*;

public class MethodHandleConstants {
    public static void main(String... av) throws Throwable {
        if (av.length > 0 && av[0].equals("--check-output"))  openBuf();
        if (av.length > 0 && av[0].equals("--security-manager"))  setSM();
        System.out.println("Obtaining method handle constants:");
        testCase(MH_String_replace_C2(), String.class, "replace", String.class, String.class, char.class, char.class);
        testCase(MH_MethodHandle_invokeExact_SC2(), MethodHandle.class, "invokeExact", String.class, MethodHandle.class, String.class, char.class, char.class);
        testCase(MH_MethodHandle_invoke_SC2(), MethodHandle.class, "invoke", String.class, MethodHandle.class, String.class, char.class, char.class);
        testCase(MH_Class_forName_S(), Class.class, "forName", Class.class, String.class);
        testCase(MH_Class_forName_SbCL(), Class.class, "forName", Class.class, String.class, boolean.class, ClassLoader.class);
        System.out.println("Done.");
        closeBuf();
    }

    private static void testCase(MethodHandle mh, Class<?> defc, String name, Class<?> rtype, Class<?>... ptypes) throws Throwable {
        System.out.println(mh);
        
        MethodType mt = methodType(rtype, ptypes);
        assertEquals(mh.type(), mt);
        
    }
    private static void assertEquals(Object exp, Object act) {
        if (exp == act || (exp != null && exp.equals(act)))  return;
        throw new AssertionError("not equal: "+exp+", "+act);
    }

    private static void setSM() {
        Policy.setPolicy(new TestPolicy());
        System.setSecurityManager(new SecurityManager());
    }

    private static PrintStream oldOut;
    private static ByteArrayOutputStream buf;
    private static void openBuf() {
        oldOut = System.out;
        buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
    }
    private static void closeBuf() {
        if (buf == null)  return;
        System.out.flush();
        System.setOut(oldOut);
        String[] haveLines = new String(buf.toByteArray()).split("[\n\r]+");
        for (String line : haveLines)  System.out.println(line);
        Iterator<String> iter = Arrays.asList(haveLines).iterator();
        for (String want : EXPECT_OUTPUT) {
            String have = iter.hasNext() ? iter.next() : "[EOF]";
            if (want.equals(have))  continue;
            System.err.println("want line: "+want);
            System.err.println("have line: "+have);
            throw new AssertionError("unexpected output: "+have);
        }
        if (iter.hasNext())
            throw new AssertionError("unexpected output: "+iter.next());
    }
    private static final String[] EXPECT_OUTPUT = {
        "Obtaining method handle constants:",
        "MethodHandle(String,char,char)String",
        "MethodHandle(MethodHandle,String,char,char)String",
        "MethodHandle(MethodHandle,String,char,char)String",
        "MethodHandle(String)Class",
        "MethodHandle(String,boolean,ClassLoader)Class",
        "Done."
    };

    
    private static MethodType MT_String_replace_C2() {
        shouldNotCallThis();
        return methodType(String.class, char.class, char.class);
    }
    private static MethodHandle MH_String_replace_C2() throws ReflectiveOperationException {
        shouldNotCallThis();
        return lookup().findVirtual(String.class, "replace", MT_String_replace_C2());
    }

    
    private static MethodType MT_MethodHandle_invokeExact_SC2() {
        shouldNotCallThis();
        return methodType(String.class, String.class, char.class, char.class);
    }
    private static MethodHandle MH_MethodHandle_invokeExact_SC2() throws ReflectiveOperationException {
        shouldNotCallThis();
        return lookup().findVirtual(MethodHandle.class, "invokeExact", MT_MethodHandle_invokeExact_SC2());
    }

    
    private static MethodType MT_MethodHandle_invoke_SC2() {
        shouldNotCallThis();
        return methodType(String.class, String.class, char.class, char.class);
    }
    private static MethodHandle MH_MethodHandle_invoke_SC2() throws ReflectiveOperationException {
        shouldNotCallThis();
        return lookup().findVirtual(MethodHandle.class, "invoke", MT_MethodHandle_invoke_SC2());
    }

    
    private static MethodType MT_Class_forName_S() {
        shouldNotCallThis();
        return methodType(Class.class, String.class);
    }
    private static MethodHandle MH_Class_forName_S() throws ReflectiveOperationException {
        shouldNotCallThis();
        return lookup().findStatic(Class.class, "forName", MT_Class_forName_S());
    }

    
    private static MethodType MT_Class_forName_SbCL() {
        shouldNotCallThis();
        return methodType(Class.class, String.class, boolean.class, ClassLoader.class);
    }
    private static MethodHandle MH_Class_forName_SbCL() throws ReflectiveOperationException {
        shouldNotCallThis();
        return lookup().findStatic(Class.class, "forName", MT_Class_forName_SbCL());
    }

    private static void shouldNotCallThis() {
        
        if (System.getProperty("MethodHandleConstants.allow-untransformed") != null)  return;
        throw new AssertionError("this code should be statically transformed away by Indify");
    }

    static class TestPolicy extends Policy {
        static final Policy DEFAULT_POLICY = Policy.getPolicy();

        final PermissionCollection permissions = new Permissions();
        TestPolicy() {
            permissions.add(new java.io.FilePermission("<<ALL FILES>>", "read"));
        }
        public PermissionCollection getPermissions(ProtectionDomain domain) {
            return permissions;
        }

        public PermissionCollection getPermissions(CodeSource codesource) {
            return permissions;
        }

        public boolean implies(ProtectionDomain domain, Permission perm) {
            return permissions.implies(perm) || DEFAULT_POLICY.implies(domain, perm);
        }
    }
}
