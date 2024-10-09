

package test.jfr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jdk.jfr.MetadataDefinition;

@MetadataDefinition
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@ModularizedAnnotation("hello annotation")
public @interface ModularizedAnnotation {

    String value();
}
