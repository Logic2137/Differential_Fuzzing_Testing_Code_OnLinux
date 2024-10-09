package org.openj9.test.annotationClassLoader;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation2 {
	String value() default "Annotation2Default";
}
