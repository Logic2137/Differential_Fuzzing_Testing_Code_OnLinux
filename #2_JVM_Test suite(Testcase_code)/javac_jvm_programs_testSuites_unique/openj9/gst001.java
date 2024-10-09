
package com.ibm.jvmti.tests.getStackTrace;

public class gst001 
{
	native static boolean check();

	public boolean testGetStackTrace() 
	{
	        return check();
	}
	
	public String helpGetStackTrace()
	{
		return "Check correct return of a known stack trace shape";		
	}
	
}
