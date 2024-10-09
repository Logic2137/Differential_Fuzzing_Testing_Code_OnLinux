

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface ParameterAnnotation {
    
    static final String STRING_VALUE_1 = "String1";
    static final String INT_VALUE_1    = "Int1";

    
    static final String STRING_VALUE_2 = "String2";
    static final String INT_VALUE_2    = "Int2";

    String value();
}
