
package com.ibm.jvmti.tests.log;

public class log001 
{
	public boolean testLogOptions()
	{
		boolean rc = false;
		
		System.out.println("testSetLogOptions");
		rc = trySetLogOptions();
		if (rc == true) {
			System.out.println("success [trySetLogOptions]");
		} else {
			System.out.println("error [trySetLogOptions]");
		}
		System.out.println("done trySetLogOptions");

		rc = tryQueryLogOptions();
		if (rc == true) {
			System.out.println("success [trySetLogOptions]");
		} else {
			System.out.println("error [trySetLogOptions]");
		}
		System.out.println("done trySetLogOptions");

		return rc;
	}

	public String helpLogOptions()
	{
		return "Test the jvmti log settings extension API";
	}

	static public native boolean tryQueryLogOptions();
	static public native boolean trySetLogOptions();
}
