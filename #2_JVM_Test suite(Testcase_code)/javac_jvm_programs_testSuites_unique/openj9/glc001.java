
package com.ibm.jvmti.tests.getLoadedClasses;

public class glc001 
{
	native static boolean check();

	
	synchronized public boolean testSingleShot() 
	{
	     return check();
	}
	
	public String helpSingleShot()
	{
		return "Check whether a single GetLoadedClasses call works correctly";		
	}
	
}
