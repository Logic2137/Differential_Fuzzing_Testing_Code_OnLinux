
package com.ibm.jvmti.tests.redefineClasses;

public class rc005_testConstantPoolChanges_R1
{
	static int staticInt = 0xBABE;
	int int1 = 0;
	static {
		staticInt = meth3();
	}
	
	public int meth4() {
		return 42;
	}

	public int meth2() {
		return meth1()*2;
	}
	
	public int meth1() {
		return 123;			
	}

	public static int meth3() {
		return staticInt;
	}
}

