
package com.ibm.jvmti.tests.getStackTrace;

public class gst002 {
	
	native static boolean check(Thread t);

	public boolean testGetUnstartedThreadStackTrace() 
	{
		Thread t = new Thread() { public void run() { } };
				
	    return check(t);
	}
	
	public String helpGetUnstartedThreadStackTrace()
	{
		return "Check return of an empty stack for a thread that has not yet been started.";		
	}
	

	public boolean testGetDeadThreadStackTrace() 
	{
		Thread t = new Thread() { public void run() { } };
		
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		
		
	    return check(t);
	}
	
	public String helpGetDeadThreadStackTrace()
	{
		return "Check return of an empty stack for a dead thread.";		
	}
	

	
	
	
}
