

package pkg;

import java.lang.annotation.*;


@Documented public @interface AnnotationType {

    
    String optional() default "unknown";

   
    int required();
}
