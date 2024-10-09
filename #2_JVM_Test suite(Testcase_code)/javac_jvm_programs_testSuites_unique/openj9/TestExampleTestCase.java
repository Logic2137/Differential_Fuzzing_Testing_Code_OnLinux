
package com.ibm.jvmti.tests.exampleTestCase;




public class TestExampleTestCase 
{
	
	public boolean testCase1()
	{
		return true;
	}
	
	public String helpCase1()
	{
		return "Mandatory help/description for testCase1";
	}
	
	
	public boolean testCase2()
	{
		return true;
	}
	public String helpCase2()
	{
		return "Mandatory help/description for testCase2";
	}
		
	public boolean testCase3()
	{
		return false;
	}
	public String helpCase3()
	{
		return "Mandatory help/description for testCase3";
	}
	
	
	
	public boolean setup(String args)
	{
		return true;
	}
	
	
	public boolean teardown()
	{
		return true;
	}
	
}
