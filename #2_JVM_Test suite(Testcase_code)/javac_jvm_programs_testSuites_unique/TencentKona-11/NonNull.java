

package java.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({FIELD, LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNull {
}
