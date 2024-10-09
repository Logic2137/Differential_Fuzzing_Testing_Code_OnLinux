
package pkg;

import java.lang.annotation.Documented;

@Documented
public @interface TestAnnotation {

    String optional() default "unknown";

    @Deprecated(forRemoval = true, since = "5")
    int required();

    int field = 0;
}
