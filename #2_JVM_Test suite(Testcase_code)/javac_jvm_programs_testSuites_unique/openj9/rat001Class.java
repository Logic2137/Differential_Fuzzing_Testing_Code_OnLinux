
package com.ibm.jvmti.tests.removeAllTags;

public class rat001Class 
{
	private String className = "rat001Class";
	
	public char[] getName()
	{
		return this.getClass().getName().toCharArray();
	}
}
