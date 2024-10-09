
package com.ibm.j9.jsr292;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

public class SamePackageExample2 {

	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}

}
