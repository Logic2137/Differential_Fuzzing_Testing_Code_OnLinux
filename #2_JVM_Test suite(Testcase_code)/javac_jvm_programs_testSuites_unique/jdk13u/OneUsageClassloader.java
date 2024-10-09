
package metaspace.staticReferences;

public class OneUsageClassloader extends ClassLoader {

    public Class<?> define(byte[] bytecode) {
        return defineClass(null, bytecode, 0, bytecode.length);
    }
}
