
package com.ibm.jvmti.tests.getPotentialCapabilities;

public class gpc002 
{
	native static boolean verifyCapabilityRetention();
		
	public boolean testCapabilityRetention()
	{
		return verifyCapabilityRetention();
	}
	
	public String helpCapabilityRetention()
	{
		return "Test retention of capabilities acquire in ON_LOAD";
	}
	
	
	
}
