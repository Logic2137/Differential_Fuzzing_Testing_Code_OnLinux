
package com.ibm.jvmti.tests.redefineClasses;

public class rc014_testModifyFinalizerForwarder_O1 
{
	static int staticInt = 0xBABE;
	int int1 = 0;

	protected void finalize () throws Throwable {
		super.finalize();
	}
}

