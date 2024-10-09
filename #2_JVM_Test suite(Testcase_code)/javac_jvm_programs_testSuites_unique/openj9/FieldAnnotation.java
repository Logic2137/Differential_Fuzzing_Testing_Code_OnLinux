package org.openj9.test.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited()
public @interface FieldAnnotation {
	String value() default "FieldAnnotationDefault";
}
