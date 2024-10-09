
package subpackage;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedNonRepeated {

    int value() default 20;
}
