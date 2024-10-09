



package compiler.jsr292;

import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

public class CreatesInterfaceDotEqualsCallInfo {
    public static void main(String[] args) throws Throwable {
        MethodHandles.publicLookup()
            .unreflect(Path.class.getMethod("toString", new Class[]{}))
            .invoke(Path.of("."));

        System.out.println("PASS, did not crash calling interface method handle");
    }
}
