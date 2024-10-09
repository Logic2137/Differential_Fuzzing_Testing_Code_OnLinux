
package com.ibm.jvmti.tests.redefineClasses;

public class rc006_testMethodIDsAfterRedefine_R1 
{
	static int staticInt = 0xBEBE;
	int int1 = 321;
	static {
		staticInt = 0xED;
	}

	public rc006_testMethodIDsAfterRedefine_R1 meth2() {
		return this;
	}

	public String meth1() {
		return "!" + int1;
	}
}

