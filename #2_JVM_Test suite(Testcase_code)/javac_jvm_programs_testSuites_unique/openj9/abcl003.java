
package com.ibm.jvmti.tests.addToBootstrapClassLoaderSearch;

public class abcl003 {
	
	public String helpAddToBootpathDuringLiveBadJar()
	{
		return "Check that AddToBootstrapClassLoaderSearch during Live correctly rejects bad jars. " +
		       "Added as a unit test for J9 VM design ID 450";		
	}

	private native boolean addJar();

	
	public boolean testAddToBootpathDuringLiveBadJar()
	{
		return addJar();
	}

}
