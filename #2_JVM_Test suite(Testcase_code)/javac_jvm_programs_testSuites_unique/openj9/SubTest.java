
package com.ibm.jvmti.tests.util;

public class SubTest 
{
	public String name = null;
	public String errors = new String();
	public String helpMethodName = null;
	public boolean result = false;
	
	
	public SubTest(String methodName)
	{		
		name = methodName;
	}
	
	public void appendError(String errorMessage)
	{
		errors += errorMessage;
	}
	
	public String getMethodName()
	{
		return name;
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public void setResult(boolean r)
	{
		result = r;
	}
	
	public void setHelpMethodName(String name)
	{
		helpMethodName = name;
	}
}
