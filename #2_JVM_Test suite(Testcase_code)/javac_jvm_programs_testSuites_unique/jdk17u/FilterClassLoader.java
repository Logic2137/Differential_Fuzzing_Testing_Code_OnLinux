
package jdk.test.lib.classloader;

import java.util.function.Predicate;

public class FilterClassLoader extends ClassLoader {

    private final ClassLoader target;

    private final Predicate<String> condition;

    public FilterClassLoader(ClassLoader target, ClassLoader parent, Predicate<String> condition) {
        super(parent);
        this.condition = condition;
        this.target = target;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (condition.test(name)) {
            return target.loadClass(name);
        }
        return super.loadClass(name);
    }
}
