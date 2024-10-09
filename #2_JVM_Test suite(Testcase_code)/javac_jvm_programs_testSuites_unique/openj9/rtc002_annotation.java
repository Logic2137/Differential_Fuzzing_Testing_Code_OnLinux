
package com.ibm.jvmti.tests.retransformClasses;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
public @interface rtc002_annotation { }
