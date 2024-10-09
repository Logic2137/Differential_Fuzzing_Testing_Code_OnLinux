
package com.ibm.j9.jsr292.indyn.bootpath;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;

public class CodeNotTrusted {
	public static CallSite bootstrap_test_privAction(Lookup lookup, String name, MethodType type) throws Throwable {
		System.out.println("user.home = " + System.getProperty("user.home"));

		MethodHandle handle = MethodHandles.constant(Object.class, null).asType(MethodType.methodType(void.class));
		return new ConstantCallSite(handle);
	}

	public static void voidMethod() {
	}
}
