
package typeannos;

import java.lang.annotation.*;

class Parameters {

    void unannotated(ParaParameterized<String, String> a) {
    }

    void firstTypeArg(ParaParameterized<@ParamA String, String> a) {
    }

    void secondTypeArg(ParaParameterized<String, @ParamA String> a) {
    }

    void bothTypeArgs(ParaParameterized<@ParamA String, @ParamB String> both) {
    }

    void nestedParaParameterized(ParaParameterized<@ParamA ParaParameterized<@ParamA String, @ParamB String>, @ParamB String> a) {
    }

    void array1(@ParamA String[] a) {
    }

    void array1Deep(@ParamA String @ParamB [] a) {
    }

    void array2(@ParamA String[][] a) {
    }

    void array2Deep(@ParamA String @ParamA [] @ParamB [] a) {
    }

    void array2First(String @ParamA [][] a) {
    }

    void array2Second(String[] @ParamB [] a) {
    }
}

class ParaParameterized<K, V> {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ParamA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface ParamB {
}
