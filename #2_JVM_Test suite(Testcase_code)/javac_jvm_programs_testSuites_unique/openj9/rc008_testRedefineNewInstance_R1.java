
package com.ibm.jvmti.tests.redefineClasses;

public class rc008_testRedefineNewInstance_R1 {
	private String value;

	public rc008_testRedefineNewInstance_R1() {
		value = "after";
	}

	public String getValue() {
		return value;
	}
}
