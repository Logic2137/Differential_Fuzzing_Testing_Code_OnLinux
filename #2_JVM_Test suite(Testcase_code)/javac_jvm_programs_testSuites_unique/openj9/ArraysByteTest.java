package org.openj9.test.annotationIdentity;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArraysByteTest {
	byte[] value();
}
