
package com.ibm.jvmti.tests.iterateOverInstancesOfClass;

public class ioioc001Class 
{
	private String className = "ioioc001Class";
	
	public char[] getName()
	{
		return this.getClass().getName().toCharArray();
	}
}
