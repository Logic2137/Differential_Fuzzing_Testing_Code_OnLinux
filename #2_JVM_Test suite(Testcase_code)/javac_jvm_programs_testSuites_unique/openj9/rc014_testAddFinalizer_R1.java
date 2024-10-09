
package com.ibm.jvmti.tests.redefineClasses;

public class rc014_testAddFinalizer_R1 
{
	static int staticInt = 0xBABE;
	int int1 = 0;

	public int meth1()
	{
		return 100;			
	}	

	protected void finalize() throws Throwable {
		
		int1 = 2;
	}
}

