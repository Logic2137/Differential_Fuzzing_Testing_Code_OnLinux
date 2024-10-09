
package annot;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target({ TYPE, MODULE, PACKAGE })
public @interface AnnotatedElementInfo {

    String annotationName();

    int expectedSize();

    String[] names();
}
