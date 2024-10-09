package org.openj9.test.pmr.failures;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanAnnotation {
	boolean val1();
	boolean val2();
}
