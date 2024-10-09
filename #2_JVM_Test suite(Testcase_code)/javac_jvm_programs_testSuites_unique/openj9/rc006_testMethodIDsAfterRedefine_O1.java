
package com.ibm.jvmti.tests.redefineClasses;

public class rc006_testMethodIDsAfterRedefine_O1 
{
	static int staticInt = 0xBEBE;
	int int1 = 123;
	static {
		staticInt = 0xED;
	}

	public String meth1() {
		return int1 + "!";
	}

	public rc006_testMethodIDsAfterRedefine_O1 meth2() {
		return this;
	}
}

