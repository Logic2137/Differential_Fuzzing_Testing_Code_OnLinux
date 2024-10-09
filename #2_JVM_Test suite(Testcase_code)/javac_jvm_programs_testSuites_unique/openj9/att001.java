
package com.ibm.jvmti.tests.attachOptionsTest;

public class att001 
{
	

	public boolean testAttachOptionsSanity()
	{				
		System.err.println("attach options sanity test passed");
		
		return true;		
	}
	
	public String helpMethodExitFromJava()
	{
		return "Test firing of the MethodExitNoRc event on an exit from a Java method";
	}	
}
