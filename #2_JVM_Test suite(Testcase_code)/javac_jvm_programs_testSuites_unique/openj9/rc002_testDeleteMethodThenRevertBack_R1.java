
package com.ibm.jvmti.tests.redefineClasses;

public class rc002_testDeleteMethodThenRevertBack_R1 
{
	static int staticInt = 0xBABE;
	int int1 = 0;

	public int meth1()
	{
		return 100;			
	}
}

