
package com.ibm.jvmti.tests.eventClassFileLoadHook;

public class ecflh001 
{
	public static native boolean checkReenableInLivePhase();
	
	public boolean testLivePhaseReenable() 
	{
		return checkReenableInLivePhase();
	}
	
	public String helpLivePhaseReenable()
	{
		return "Enable and Disable the ClassFileLoadHook event in OnLoad phase and attempt to reenable in Live phase";
	}
	
}
