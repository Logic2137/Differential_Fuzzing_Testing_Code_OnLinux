
package com.ibm.jvmti.tests.redefineClasses;

public class rc004_testStaticFieldIDsAfterRedefine_R1 
{
	public static int int2 = 456;
	public static String f1 = "def";

	public int meth1()
	{
		return -1 + (int) f1.hashCode();
	}
}
