
package com.ibm.jvmti.tests.getClassVersionNumbers;

public class gcvn001
{
	public static native boolean checkVersionNumbers(int major, int minor);

	public boolean testGetClassVersion()
	{
		
		int major = 52;
		int minor = 0;

		boolean ret = checkVersionNumbers(minor, major);

		return ret;

	}

	public String helpGetClassVersion()
	{
		return "Obtains the version of a class known to be compiled using a specific -target version";
	}

}
