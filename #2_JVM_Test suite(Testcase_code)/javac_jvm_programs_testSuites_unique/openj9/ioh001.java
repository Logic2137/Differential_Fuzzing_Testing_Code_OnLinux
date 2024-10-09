
package com.ibm.jvmti.tests.iterateOverHeap;

public class ioh001 
{
	
	public String helpClassInstanceTraversal()
	{
		return "Check the IterateOverHeap API for correct traversal of instances of java/lang/Class. " +
		       "Added as a unit test for CMVC 111022";
		
	}
		
	public boolean testClassInstanceTraversal()
	{
		return iterate();
	}
	
	public static native boolean iterate();
	
}
