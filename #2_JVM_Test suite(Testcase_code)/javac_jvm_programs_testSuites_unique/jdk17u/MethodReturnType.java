
package typeannos;

import java.lang.annotation.*;

class MtdDefaultScope {

    MtdParameterized<String, String> unannotated() {
        return null;
    }

    MtdParameterized<@MRtnA String, String> firstTypeArg() {
        return null;
    }

    MtdParameterized<String, @MRtnA String> secondTypeArg() {
        return null;
    }

    MtdParameterized<@MRtnA String, @MRtnB String> bothTypeArgs() {
        return null;
    }

    MtdParameterized<@MRtnA MtdParameterized<@MRtnA String, @MRtnB String>, @MRtnB String> nestedMtdParameterized() {
        return null;
    }

    @MRtnA
    public <T> String method() {
        return null;
    }

    @MRtnA
    String[] array1() {
        return null;
    }

    @MRtnA
    String @MRtnB [] array1Deep() {
        return null;
    }

    @MRtnA
    String[][] array2() {
        return null;
    }

    @MRtnA
    String @MRtnA [] @MRtnB [] array2Deep() {
        return null;
    }

    String @MRtnA [][] array2First() {
        return null;
    }

    String[] @MRtnB [] array2Second() {
        return null;
    }

    String @MRtnA [] array2FirstOld() {
        return null;
    }

    String[] @MRtnB [] array2SecondOld() {
        return null;
    }
}

class MtdModifiedScoped {

    public final MtdParameterized<String, String> unannotated() {
        return null;
    }

    public final MtdParameterized<@MRtnA String, String> firstTypeArg() {
        return null;
    }

    public final MtdParameterized<String, @MRtnA String> secondTypeArg() {
        return null;
    }

    public final MtdParameterized<@MRtnA String, @MRtnB String> bothTypeArgs() {
        return null;
    }

    public final MtdParameterized<@MRtnA MtdParameterized<@MRtnA String, @MRtnB String>, @MRtnB String> nestedMtdParameterized() {
        return null;
    }

    @MRtnA
    public final String[] array1() {
        return null;
    }

    @MRtnA
    public final String @MRtnB [] array1Deep() {
        return null;
    }

    @MRtnA
    public final String[][] array2() {
        return null;
    }

    @MRtnA
    public final String @MRtnA [] @MRtnB [] array2Deep() {
        return null;
    }

    public final String @MRtnA [][] array2First() {
        return null;
    }

    public final String[] @MRtnB [] array2Second() {
        return null;
    }
}

class MtdParameterized<K, V> {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface MRtnA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface MRtnB {
}
