
package com.ibm.jvmti.tests.redefineClasses;

public class rc001_testStaticFixups_O1 
{
	public static int staticInt = 100;     
	
	
	
	public int getStaticInt()
	{
		return staticInt;
	}
	
	public String getClassVersion()
	{
		return "original";
		
	}
	
}
