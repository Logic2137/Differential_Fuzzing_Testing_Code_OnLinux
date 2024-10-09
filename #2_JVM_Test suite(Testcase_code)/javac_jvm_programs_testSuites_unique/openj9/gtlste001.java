
package com.ibm.jvmti.tests.getThreadListStackTracesExtended;

public class gtlste001 
{
	

	static long    ii;
	static int     iter         = 10000;   
	static int     reached_iter = 0;
	static boolean passed       = false;

	public String helpGetThreadListStackTracesExtended()
	{
		return "Check the jvmtiGetThreaListdStackTracesExtended API for at least 1 jitted frame after a relatively large number of iterations " +
		       "Added as a unit test for J9 VM design ID 771";		
	}			
	
	public boolean testGetThreadListStackTracesExtended()
	{
		while (reached_iter < iter) {
			reached_iter++;
			myA();
		}
		return passed;
	}

	
	public static native int anyJittedFrame(int high);
	
	static void myA()
	{
		myB();
	}

	static void myB()
	{
		int ret;
		
		for (int j = 0; j < 1000; j++) {
			ii++;
		}
		
		if (reached_iter == iter) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
						
			ret = anyJittedFrame(1);
			if (ret == 2) {
				myB();
			}
			
			if (ret == 0) {
				passed = false;
			} else {
				passed = true;			
			}
		}
	}
}
