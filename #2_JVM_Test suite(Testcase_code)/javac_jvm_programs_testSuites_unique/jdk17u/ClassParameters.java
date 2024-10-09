
package typeannos;

import java.lang.annotation.*;

class Unannotated<K> {
}

class ExtendsBound<K extends @ClassParamA String> {
}

class ExtendsGeneric<K extends @ClassParamA Unannotated<@ClassParamB String>> {
}

class TwoBounds<K extends @ClassParamA String, V extends @ClassParamB String> {
}

class Complex1<K extends @ClassParamA String & Runnable> {
}

class Complex2<K extends String & @ClassParamB Runnable> {
}

class ComplexBoth<K extends @ClassParamA String & @ClassParamA Runnable> {
}

class ClassParamOuter {

    void inner() {
        class LUnannotated<K> {
        }
        class LExtendsBound<K extends @ClassParamA String> {
        }
        class LExtendsGeneric<K extends @ClassParamA LUnannotated<@ClassParamB String>> {
        }
        class LTwoBounds<K extends @ClassParamA String, V extends @ClassParamB String> {
        }
        class LComplex1<K extends @ClassParamA String & Runnable> {
        }
        class LComplex2<K extends String & @ClassParamB Runnable> {
        }
        class LComplexBoth<K extends @ClassParamA String & @ClassParamA Runnable> {
        }
    }
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ClassParamA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ClassParamB {
}
