import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@B
class TypeUseTarget<K extends @B Object> {

    @B
    String @B [] field;

    @B
    String test(@B TypeUseTarget<K> this, @B String param, @B String@B ... vararg) {
        @B
        Object o = new @B String @B [3];
        TypeUseTarget<@B String> target;
        return (@B String) null;
    }

    @B
    <K> String genericMethod(K k) {
        return null;
    }

    @Decl
    @B
    <K> String genericMethod1(K k) {
        return null;
    }

    @B
    @Decl
    <K> String genericMethod2(K k) {
        return null;
    }

    @Decl
    @B
    <K> String genericMethod3(K k) {
        return null;
    }

    @Decl
    <K> String genericMethod4(K k) {
        return null;
    }

    @B
    @Decl
    <K> String genericMethod5(K k) {
        return null;
    }
}

@B
interface MyInterface {
}

@B
@interface MyAnnotation {
}

@Target(ElementType.TYPE_USE)
@interface B {
}

@interface Decl {
}
