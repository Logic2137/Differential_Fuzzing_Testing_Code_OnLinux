import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckOverrides {

    public static void main(String[] args) {
        Set<MethodSignature> pMethodSignatures = Stream.of(Properties.class.getDeclaredMethods()).filter(CheckOverrides::isMethodOfInterest).map(MethodSignature::new).collect(Collectors.toSet());
        Map<MethodSignature, Method> unoverriddenMethods = new HashMap<>();
        for (Class<?> superclass = Properties.class.getSuperclass(); superclass != Object.class; superclass = superclass.getSuperclass()) {
            Stream.of(superclass.getDeclaredMethods()).filter(CheckOverrides::isMethodOfInterest).forEach(m -> unoverriddenMethods.putIfAbsent(new MethodSignature(m), m));
        }
        unoverriddenMethods.keySet().removeAll(pMethodSignatures);
        if (!unoverriddenMethods.isEmpty()) {
            throw new RuntimeException("The following methods should be overridden by Properties class:\n" + unoverriddenMethods.values().stream().map(Method::toString).collect(Collectors.joining("\n  ", "  ", "\n")));
        }
    }

    static boolean isMethodOfInterest(Method method) {
        int mods = method.getModifiers();
        return !Modifier.isStatic(mods) && (Modifier.isPublic(mods) || Modifier.isProtected(mods));
    }

    static class MethodSignature {

        final Class<?> returnType;

        final String name;

        final Class<?>[] parameterTypes;

        MethodSignature(Method method) {
            this(method.getReturnType(), method.getName(), method.getParameterTypes());
        }

        private MethodSignature(Class<?> returnType, String name, Class<?>[] parameterTypes) {
            this.returnType = returnType;
            this.name = name;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MethodSignature that = (MethodSignature) o;
            if (!returnType.equals(that.returnType))
                return false;
            if (!name.equals(that.name))
                return false;
            return Arrays.equals(parameterTypes, that.parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = returnType.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }
    }
}
