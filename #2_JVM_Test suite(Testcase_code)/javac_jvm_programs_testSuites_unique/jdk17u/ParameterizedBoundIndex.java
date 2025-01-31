import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class ParameterizedBoundIndex {

    public static void main(String[] args) throws Exception {
        List<Class<?>> failed = new ArrayList<>();
        if (!TypeClassBound.class.getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(TypeClassBound.class);
        }
        if (!TypeInterfaceBound.class.getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(TypeInterfaceBound.class);
        }
        if (!TypeParameterizedInterfaceBound.class.getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(TypeParameterizedInterfaceBound.class);
        }
        if (!TypeParameterizedClassBound.class.getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(TypeParameterizedClassBound.class);
        }
        if (!TypeVariableBound.class.getTypeParameters()[1].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(TypeVariableBound.class);
        }
        if (!MethodClassBound.class.getDeclaredMethod("m").getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(MethodClassBound.class);
        }
        if (!MethodInterfaceBound.class.getDeclaredMethod("m").getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(MethodInterfaceBound.class);
        }
        if (!MethodParameterizedInterfaceBound.class.getDeclaredMethod("m").getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(MethodParameterizedInterfaceBound.class);
        }
        if (!MethodParameterizedClassBound.class.getDeclaredMethod("m").getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(MethodParameterizedClassBound.class);
        }
        if (!MethodVariableBound.class.getDeclaredMethod("m").getTypeParameters()[1].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(MethodVariableBound.class);
        }
        if (!ConstructorClassBound.class.getDeclaredConstructor().getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(ConstructorClassBound.class);
        }
        if (!ConstructorInterfaceBound.class.getDeclaredConstructor().getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(ConstructorInterfaceBound.class);
        }
        if (!ConstructorParameterizedInterfaceBound.class.getDeclaredConstructor().getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(ConstructorParameterizedInterfaceBound.class);
        }
        if (!ConstructorParameterizedClassBound.class.getDeclaredConstructor().getTypeParameters()[0].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(ConstructorParameterizedClassBound.class);
        }
        if (!ConstructorVariableBound.class.getDeclaredConstructor().getTypeParameters()[1].getAnnotatedBounds()[0].isAnnotationPresent(TypeAnnotation.class)) {
            failed.add(ConstructorVariableBound.class);
        }
        if (!failed.isEmpty()) {
            throw new RuntimeException("Failed: " + failed);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface TypeAnnotation {
    }

    static class TypeClassBound<T extends @TypeAnnotation Void> {
    }

    static class TypeInterfaceBound<T extends @TypeAnnotation Runnable> {
    }

    static class TypeParameterizedInterfaceBound<T extends @TypeAnnotation List<?>> {
    }

    static class TypeParameterizedClassBound<T extends @TypeAnnotation ArrayList<?>> {
    }

    static class TypeVariableBound<T, S extends @TypeAnnotation T> {
    }

    static class MethodClassBound<T extends @TypeAnnotation Void> {

        <T extends @TypeAnnotation Void> void m() {
        }
    }

    static class MethodInterfaceBound {

        <T extends @TypeAnnotation Runnable> void m() {
        }
    }

    static class MethodParameterizedInterfaceBound<T extends @TypeAnnotation List<?>> {

        <T extends @TypeAnnotation List<?>> void m() {
        }
    }

    static class MethodParameterizedClassBound {

        <T extends @TypeAnnotation ArrayList<?>> void m() {
        }
    }

    static class MethodVariableBound<T, S extends @TypeAnnotation T> {

        <T, S extends @TypeAnnotation T> void m() {
        }
    }

    static class ConstructorClassBound<T extends @TypeAnnotation Void> {

        <T extends @TypeAnnotation Void> ConstructorClassBound() {
        }
    }

    static class ConstructorInterfaceBound {

        <T extends @TypeAnnotation Runnable> ConstructorInterfaceBound() {
        }
    }

    static class ConstructorParameterizedInterfaceBound<T extends @TypeAnnotation List<?>> {

        <T extends @TypeAnnotation List<?>> ConstructorParameterizedInterfaceBound() {
        }
    }

    static class ConstructorParameterizedClassBound {

        <T extends @TypeAnnotation ArrayList<?>> ConstructorParameterizedClassBound() {
        }
    }

    static class ConstructorVariableBound<T, S extends @TypeAnnotation T> {

        <T, S extends @TypeAnnotation T> ConstructorVariableBound() {
        }
    }
}
