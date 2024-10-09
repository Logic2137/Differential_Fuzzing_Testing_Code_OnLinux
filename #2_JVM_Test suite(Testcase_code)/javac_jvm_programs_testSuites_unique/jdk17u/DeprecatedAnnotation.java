
package jdk.deprcases.types;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD, FIELD })
@Deprecated
public @interface DeprecatedAnnotation {

    String file() default "x";

    String value() default "y";
}
