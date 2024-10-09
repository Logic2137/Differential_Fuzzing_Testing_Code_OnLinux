
package com.ibm.jvmti.tests.getMethodAndClassNames;

public class gmcpn001 {
	
	native static boolean check(int type);
	
	final static private int type_single = 1;
	final static private int type_multiple = 2;
	final static private int type_invalid_single = 3;
	
	public boolean testSingleRamMethod()
	{		
		return check(type_single);
	}

	public String helpSingleRamMethod()
	{
		return "tests retrieval of class name, method name and package name for a single ram method pointer";
	}

	
	public boolean testMultipleRamMethod()
	{		
		return check(type_multiple);
	}

	public String helpMultipleRamMethod()
	{
		return "tests retrieval of class name, method name and package name for multiple ram method pointers";
	}

	
	public void singleMethod1()
	{
		
	}
	public void singleMethod2()
	{
		
	}
	public void singleMethod3()
	{
		
	}

}
