
package com.ibm.j9.jsr292;
import static java.lang.invoke.MethodHandles.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;


class PrivateMethod {
	@SuppressWarnings("unused")
	private static String method() {
		return "privateMethod";
	}
		
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
}
