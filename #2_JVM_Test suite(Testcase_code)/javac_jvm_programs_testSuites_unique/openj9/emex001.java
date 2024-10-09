
package com.ibm.jvmti.tests.eventMethodExit;

public class emex001 
{
	static public native boolean checkJavaMethodExit();
	static public native boolean checkNativeMethodExit();
	
	
	static int sampleJavaMethod(int foo)
	{
		return 100 + foo;
	}
	
	static public native int sampleNativeMethod(int foo);
	
	
	
	public boolean testMethodExitFromJava()
	{
		int ret = sampleJavaMethod(100);
		if (ret != 200) {
			return false;
		}
					
		return checkJavaMethodExit();		
	}
	
	public String helpMethodExitFromJava()
	{
		return "Test firing of the MethodExitNoRc event on an exit from a Java method";
	}
	
	public boolean testMethodExitFromNative()
	{
		int ret = sampleNativeMethod(100);
		if (ret != 200) {
			return false;
		}
			
		return checkNativeMethodExit();	
	}
	
	public String helpMethodExitFromNative()
	{
		return "Test firing of the MethodExitNoRc event on an exit from a Native method";
	}
	
	
}
