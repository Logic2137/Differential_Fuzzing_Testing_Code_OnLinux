
package com.ibm.jvmti.tests.getJ9vmThread;

public class gjvmt001 
{
	
	
	public static native boolean validateGJVMT001();
	
	
	public boolean testGetJ9vmThread() 
	{
		boolean rc = true;
		
		if (!validateGJVMT001()) {
			System.err.println("extension getJ9vmThread failed to return valid J9VMThread");
			rc = false;
		}
		
		return rc;
	}
	
	public String helpGetJ9vmThread()
	{
		return "Check getJ9vmThread returns valid J9VMThread";		
	}
	
}
