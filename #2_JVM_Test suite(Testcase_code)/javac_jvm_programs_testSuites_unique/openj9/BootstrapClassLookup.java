
package com.ibm.j9.jsr292.bootstrap;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class BootstrapClassLookup {
	public static Lookup lookup() {
		return MethodHandles.lookup();
	}
}
