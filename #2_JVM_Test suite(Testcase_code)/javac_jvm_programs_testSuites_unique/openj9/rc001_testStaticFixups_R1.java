
package com.ibm.jvmti.tests.redefineClasses;

public class rc001_testStaticFixups_R1
{
	
	public static int staticInt = 200;      
	
	
	public int getStaticInt()
	{
		return staticInt;
	}
	
	public String getClassVersion()
	{
		
		return "redefined";					
	}
	
}
