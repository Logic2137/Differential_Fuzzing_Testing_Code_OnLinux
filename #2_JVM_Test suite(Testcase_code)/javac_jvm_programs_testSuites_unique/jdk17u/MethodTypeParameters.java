
package typeannos;

import java.lang.annotation.*;

class UnscopedUnmodified {

    <K extends @MTyParamA String> void methodExtends() {
    }

    <K extends @MTyParamA MtdTyParameterized<@MTyParamB String>> void nestedExtends() {
    }

    <K extends @MTyParamA String, V extends @MTyParamA MtdTyParameterized<@MTyParamB String>> void dual() {
    }

    <K extends String, V extends MtdTyParameterized<@MTyParamB String>> void dualOneAnno() {
    }
}

class PublicModifiedMethods {

    public final <K extends @MTyParamA String> void methodExtends() {
    }

    public final <K extends @MTyParamA MtdTyParameterized<@MTyParamB String>> void nestedExtends() {
    }

    public final <K extends @MTyParamA String, V extends @MTyParamA MtdTyParameterized<@MTyParamB String>> void dual() {
    }

    public final <K extends String, V extends MtdTyParameterized<@MTyParamB String>> void dualOneAnno() {
    }
}

class MtdTyParameterized<K> {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface MTyParamA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface MTyParamB {
}
