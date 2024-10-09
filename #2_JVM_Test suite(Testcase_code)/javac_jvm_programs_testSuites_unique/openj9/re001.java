
package com.ibm.jvmti.tests.resourceExhausted;

import java.util.Vector;


public class re001 
{
	public static native boolean hasBeenCalledBack();
	
	public boolean testOutOfMemoryString()
	{
		String str = "      ";
		boolean ret = false;
		
		
		hasBeenCalledBack();
		
		try {
			while (true) {
				str = str + str;							
			}
		} 
		catch (OutOfMemoryError ex)
		{
			
			
			
			ret = hasBeenCalledBack();
		}
		
		return ret;
	}
	
	public String helpOutOfMemoryString()
	{
		return "Tests whether the VM calls back with a Resource Exhausted event (with JVMTI_RESOURCE_EXHAUSTED_OOM_ERROR flag set)";
	}
	
	
	
	
	public boolean testOutOfMemoryObject()
	{
		Vector objs = new Vector();
		boolean ret = false;
		
		
		hasBeenCalledBack();
		
		try {
			while (true) {
				objs.add(new long[1024]);			
			}
		} 
		catch (OutOfMemoryError ex)
		{
			
			
			
			ret = hasBeenCalledBack();
		}
		
		return ret;
	}
	
	public String helpOutOfMemoryObject()
	{
		return "Tests whether the VM calls back with a Resource Exhausted event (with JVMTI_RESOURCE_EXHAUSTED_OOM_ERROR flag set)";
	}

	
}
