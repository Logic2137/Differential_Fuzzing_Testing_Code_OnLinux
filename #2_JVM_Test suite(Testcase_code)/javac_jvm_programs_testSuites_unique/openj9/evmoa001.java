
package com.ibm.jvmti.tests.eventVMObjectAllocation;

public class evmoa001 
{
	native static boolean didTestRun();
	
	public String helpVMDidBootstrap()
	{
		return "Test that the VM still managed to start even though we tried to follow reference from a freshly allocated yet uninitialized java.lang.Class object.";
	}
	
	public boolean testVMDidBootstrap()
	{
		
		
		return didTestRun();
	}
}
