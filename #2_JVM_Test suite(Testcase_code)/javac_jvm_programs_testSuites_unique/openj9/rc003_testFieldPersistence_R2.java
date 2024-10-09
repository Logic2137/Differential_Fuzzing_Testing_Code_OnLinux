
package com.ibm.jvmti.tests.redefineClasses;

public class rc003_testFieldPersistence_R2 
{
	static int staticInt = 0xBABE;
	int int1 = 0;
	public static float f1 = 51f;
	public static float f2 = 2f;

	public int meth1()
	{
		return staticInt;			
	}
}

