
package com.ibm.jvmti.tests.getMemoryCategories;

public class gmc001
{

	private native boolean check();

	public boolean testGetMemoryCategories()
	{
		return check();
	}

	public String helpGetMemoryCategories()
	{
		return "Verifies that the getMemoryCategories TI extension API works with good and bad inputs";
	}
}
