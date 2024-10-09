
package com.ibm.jvmti.tests.redefineClasses;

public class rc008_testRedefineNewInstance_O1 {
	private String value;

	public rc008_testRedefineNewInstance_O1() {
		value = "before";
	}

	public String getValue() {
		return value;
	}
}
