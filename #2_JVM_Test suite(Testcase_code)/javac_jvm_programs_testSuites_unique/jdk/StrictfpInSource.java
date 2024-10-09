

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;



@Retention(RUNTIME)
@Target({METHOD, CONSTRUCTOR})
public @interface StrictfpInSource {}
