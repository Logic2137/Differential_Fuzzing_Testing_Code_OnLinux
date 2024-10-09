
package com.ibm.jvmti.tests.getPotentialCapabilities;

public class gpc001 
{
	native static boolean verifyOnLoadCapabilities();
	native static boolean verifyLiveCapabilities();
	
	public boolean testGetOnLoadCapabilities()
	{
		return verifyOnLoadCapabilities();
	}
	
	public String helpGetOnLoadCapabilities()
	{
		return "Check the set of capabilities available in the ON_LOAD phase";
	}
	
	public boolean testGetLiveCapabilities()
	{
		return verifyLiveCapabilities();
	}
	
	public String helpGetLiveCapabilities()
	{
		return "Check the set of capabilities available in the LIVE phase";
	}
	
}
