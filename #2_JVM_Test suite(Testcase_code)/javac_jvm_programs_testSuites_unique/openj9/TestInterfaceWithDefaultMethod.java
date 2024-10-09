
package com.ibm.j9.jsr292;


public interface TestInterfaceWithDefaultMethod {
	public static final Integer FORTY_TWO = 42;

	public default Integer test() {
		return FORTY_TWO;
	}
}
