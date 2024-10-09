
package vm.mlvm.meth.share;

public final class MHUtils {

    public static void assertAssignableType(Object info, Class<?> requiredType, Class<?> actualType) throws IllegalArgumentException {
        if (!requiredType.isAssignableFrom(actualType)) {
            throw new IllegalArgumentException("Illegal argument type for " + info + ": required=" + requiredType + "; actual=" + actualType);
        }
    }
}
