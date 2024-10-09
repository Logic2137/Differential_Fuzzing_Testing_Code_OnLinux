
package subpackage;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface NonRepeated {

    int value() default 10;
}
