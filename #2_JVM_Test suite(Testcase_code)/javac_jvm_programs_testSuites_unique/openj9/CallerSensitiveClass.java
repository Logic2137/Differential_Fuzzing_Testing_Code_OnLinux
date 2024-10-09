
package com.ibm.j9.jsr292.bootstrap;

public class CallerSensitiveClass extends SecurityManager {
	@sun.reflect.CallerSensitive
	public void callerSensitiveMethod() {
		Class<?> c = sun.reflect.Reflection.getCallerClass();
		if (c.getClassLoader() != null) {
			throw new SecurityException();
		}
	}
}
