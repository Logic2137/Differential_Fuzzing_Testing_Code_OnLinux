
package common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestMe {

    String desc();

    String encl();

    String simple();

    boolean hasCanonical() default true;

    String canonical() default "";
}
