
package annot;

import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(RUNTIME)
@Target({ TYPE, MODULE, PACKAGE })
public @interface MarkerAnnot {
}
