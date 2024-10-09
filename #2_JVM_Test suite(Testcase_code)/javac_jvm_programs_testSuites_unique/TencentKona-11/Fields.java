

package typeannos;

import java.lang.annotation.*;


class DefaultScope {
    Parameterized<String, String> unannotated;
    Parameterized<@FldA String, String> firstTypeArg;
    Parameterized<String, @FldA String> secondTypeArg;
    Parameterized<@FldA String, @FldB String> bothTypeArgs;

    Parameterized<@FldA Parameterized<@FldA String, @FldB String>, @FldB String>
    nestedParameterized;

    @FldA String [] array1;
    @FldA String @FldB [] array1Deep;
    @FldA String [] [] array2;
    @FldD String @FldC @FldA [] @FldC @FldB [] array2Deep;
    String @FldA [] [] array2First;
    String [] @FldB [] array2Second;

    
    String array2FirstOld @FldA [];
    String array2SecondOld [] @FldB [];
}

class ModifiedScoped {
    public final Parameterized<String, String> unannotated = null;
    public final Parameterized<@FldA String, String> firstTypeArg = null;
    public final Parameterized<String, @FldA String> secondTypeArg = null;
    public final Parameterized<@FldA String, @FldB String> bothTypeArgs = null;

    public final Parameterized<@FldA Parameterized<@FldA String, @FldB String>, @FldB String>
    nestedParameterized = null;

    public final @FldA String [] array1 = null;
    public final @FldA String @FldB [] array1Deep = null;
    public final @FldA String [] [] array2 = null;
    public final @FldA String @FldA [] @FldB [] array2Deep = null;
    public final String @FldA [] [] array2First = null;
    public final String [] @FldB [] array2Second = null;
}

class Parameterized<K, V> { }

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface FldA { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface FldB { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface FldC { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@Documented
@interface FldD { }
