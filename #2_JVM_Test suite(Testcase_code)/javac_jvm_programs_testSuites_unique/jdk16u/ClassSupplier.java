
package util;

import java.util.function.Supplier;


public enum ClassSupplier implements Supplier<Class<?>> {
    PACKAGE_CLASS_IN_PKG_A("a.Package"),
    PUBLIC_SUPERCLASS_IN_PKG_A("a.PublicSuper"),
    PACKAGE_CLASS_IN_PKG_B("b.Package"),
    PUBLIC_SUBCLASS_IN_PKG_B("b.PublicSub");

    private final String className;

    ClassSupplier(String className) {
        this.className = className;
    }

    @Override
    public Class<?> get() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw (Error) new NoClassDefFoundError(className).initCause(e);
        }
    }
}
