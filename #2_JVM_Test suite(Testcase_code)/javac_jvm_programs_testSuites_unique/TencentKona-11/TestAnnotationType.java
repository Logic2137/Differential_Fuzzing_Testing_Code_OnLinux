

package pkg2;

import java.lang.annotation.*;


@Documented public @interface TestAnnotationType {

    
    String optional() default "unknown";

   
    int required();
}
