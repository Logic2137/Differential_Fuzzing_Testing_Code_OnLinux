
package com.sun.swingset3;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DemoProperties {

    String value();

    String category();

    String description();

    String iconFile() default "";

    String[] sourceFiles() default "";
}
