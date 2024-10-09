
package com.ibm.jvmti.tests.redefineClasses;

public class rc004_testStaticFieldIDsAfterRedefine_O1 
{
	public static int int1 = 123;
	public static String f1 = "abc";

	public int meth1()
	{
		return this.getClass().hashCode();
	}

}
