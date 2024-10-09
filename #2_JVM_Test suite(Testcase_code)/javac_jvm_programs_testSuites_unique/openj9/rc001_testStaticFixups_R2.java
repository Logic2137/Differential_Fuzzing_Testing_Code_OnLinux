
package com.ibm.jvmti.tests.redefineClasses;

public class rc001_testStaticFixups_R2
{
	
	
	public static int staticInt = 300;      
	
	public int getStaticInt()
	{
		return staticInt;
	}
	
	public String getClassVersion()
	{
		
		return "redefined";					
	}
	
}
