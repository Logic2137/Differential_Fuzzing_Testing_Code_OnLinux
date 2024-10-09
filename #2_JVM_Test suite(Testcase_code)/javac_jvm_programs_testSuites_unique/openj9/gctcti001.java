
package com.ibm.jvmti.tests.getCurrentThreadCpuTimerInfo;

public class gctcti001 
{

	static public native boolean verifyTimerInfo();
	
	public boolean testGetTimerInfo()
	{
		return verifyTimerInfo();
	}
	
	public String helpGetTimerInfo()
	{
		return "Verifies return of correct static information";
	}
	
}
