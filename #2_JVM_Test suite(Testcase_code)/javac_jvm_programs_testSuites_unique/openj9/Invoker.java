
package com.ibm.j9.jsr292.bootstrap;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.WrongMethodTypeException;

public class Invoker {
	public static void invokeVirtual(MethodHandle mh, Object instance) throws WrongMethodTypeException, ClassCastException, Throwable {
		mh.invoke(instance);
	}
}
