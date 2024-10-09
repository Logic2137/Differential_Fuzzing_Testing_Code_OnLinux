
package metaspace.stressDictionary;

public class ClassloaderUnderTest extends ClassLoader {

    private static boolean isParallelCapable;

    public static boolean isParallelCapableCL() { return isParallelCapable; }
    {
        isParallelCapable = registerAsParallelCapable();
    }

    public Class<?> define(byte[] bytecode) {
        return defineClass(null, bytecode, 0 ,bytecode.length);
    }

}
