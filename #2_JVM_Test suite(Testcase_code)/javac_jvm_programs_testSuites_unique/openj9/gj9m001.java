
package com.ibm.jvmti.tests.getJ9method;

public class gj9m001 
{
	
	
	public static native boolean validateGJ9M001();
	
	
	public boolean testGetJ9method() 
	{
		boolean rc = true;
		
		if (!validateGJ9M001()) {
			System.err.println("extension getJ9method failed to return valid J9Method");
			rc = false;
		}
		
		return rc;
	}
	
	public String helpGetJ9method()
	{
		return "Check getJ9method returns valid J9Method";		
	}
	
}
