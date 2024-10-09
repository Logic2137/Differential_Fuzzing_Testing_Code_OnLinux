

package typeannos;

import java.lang.annotation.*;


class TypUnannotated<K> { }
class OneAnnotated<@TyParaA K> { }
class TwoAnnotated<@TyParaA K, @TyParaA V> { }
class SecondAnnotated<K, @TyParaA V extends String> { }

class TestMethods {
    <K> void unannotated() { }
    <@TyParaA K> void oneAnnotated() { }
    <@TyParaA K, @TyParaB("m") V> void twoAnnotated() { }
    <K, @TyParaA V extends @TyParaA String> void secondAnnotated() { }
}

class UnannotatedB<K> { }
class OneAnnotatedB<@TyParaB("m") K> { }
class TwoAnnotatedB<@TyParaB("m") K, @TyParaB("m") V> { }
class SecondAnnotatedB<K, @TyParaB("m") V extends @TyParaB("m") String> { }

class OneAnnotatedC<@TyParaC K> { }
class TwoAnnotatedC<@TyParaC K, @TyParaC V> { }
class SecondAnnotatedC<K, @TyParaC V extends String> { }

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface TyParaA { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface TyParaB { String value(); }
@Target(ElementType.TYPE_USE)
@Documented
@interface TyParaC { }
