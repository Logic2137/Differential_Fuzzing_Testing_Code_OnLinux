

package com.ibm.j9.recreateclass.testclasses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;

@Retention(RetentionPolicy.RUNTIME)
@Inherited()
public @interface RuntimeVisibleAnnotation2 {
	 Class<?> classValue() default Object.class;
}
