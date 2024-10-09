
package testpkgmdlB;

import java.lang.annotation.*;

@Target(ElementType.MODULE)
public @interface AnnotationTypeUndocumented {

    String optional() default "unknown";

    int required();
}
