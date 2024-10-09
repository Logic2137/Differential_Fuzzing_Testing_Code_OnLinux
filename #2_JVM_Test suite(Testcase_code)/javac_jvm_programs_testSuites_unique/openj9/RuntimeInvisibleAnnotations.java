

package com.ibm.j9.recreateclass.testclasses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;

@Retention(RetentionPolicy.CLASS)
@Inherited()
public @interface RuntimeInvisibleAnnotations {
	String value() default "AnnotationDefaultValue";
}
