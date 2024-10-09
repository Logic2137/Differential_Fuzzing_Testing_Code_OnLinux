
package com.ibm.jvmti.tests.addToSystemClassLoaderSearch;

public class ascl003 {
	
	public String helpAddToClasspathDuringLiveBadJar()
	{
		return "Check that AddToSystemClassLoaderSearch during Live correctly rejects bad jars. " +
		       "Added as a unit test for J9 VM design ID 450";		
	}

	private native boolean addJar();

	
	public boolean testAddToClasspathDuringLiveBadJar()
	{
		return addJar();
	}

}
