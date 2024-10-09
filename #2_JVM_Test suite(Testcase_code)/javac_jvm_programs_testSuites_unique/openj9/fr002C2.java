
package com.ibm.jvmti.tests.followReferences;

public class fr002C2 
{
	private static final long serialVersionUID = 2L;
	
	int	c2_i = 32;
	double c2_f1 = 32; 
	double c2_f2 = 32;
	short c1_common_s1 = 256;
	
	public void foo()
	{
		System.out.println("foo" + c1_common_s1);
		
		
	}
}
