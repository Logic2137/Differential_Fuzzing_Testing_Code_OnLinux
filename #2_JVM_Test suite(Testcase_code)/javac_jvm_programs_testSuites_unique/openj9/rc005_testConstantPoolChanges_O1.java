
package com.ibm.jvmti.tests.redefineClasses;

public class rc005_testConstantPoolChanges_O1 
{
	static int staticInt = 0xBEBE;
	int int1 = 0;
	static {
		staticInt = 0xED;
	}
	
	public int meth1() {
		return 100;
	}

	public int meth2() {
		return 200;
	}

	public static int meth3() {
		return 300;
	}

	public int meth4() {
		return 42;
	}
}

