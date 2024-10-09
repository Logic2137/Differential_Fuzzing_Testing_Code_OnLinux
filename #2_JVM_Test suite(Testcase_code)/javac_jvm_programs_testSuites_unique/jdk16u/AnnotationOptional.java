

package pkg;

import java.lang.annotation.*;


@Documented public @interface AnnotationOptional {
    String value() default "";
}
