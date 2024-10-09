
package linksource;

import java.lang.annotation.*;

@Documented
public @interface AnnotationTypeField {

    String DEFAULT_NAME = "test";

    String name() default DEFAULT_NAME;
}
