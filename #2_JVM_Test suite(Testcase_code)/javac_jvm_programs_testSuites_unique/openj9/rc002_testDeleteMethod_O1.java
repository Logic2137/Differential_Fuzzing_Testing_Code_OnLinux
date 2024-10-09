
package com.ibm.jvmti.tests.redefineClasses;

public class rc002_testDeleteMethod_O1 
{
	static int staticInt = 0xBABE;
	int int1 = 0;

	public int meth1()
	{
		return 100;			
	}

	public int meth2()
	{
		return 200;
	}
}

