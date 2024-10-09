
package com.ibm.jvmti.tests.eventThreadStart;

public class ets001 
{
	native static boolean check(Thread t);
	
	public String helpThreadStart()
	{
		return "Test receipt of a Thread Start event";
	}
	
	public boolean testThreadStart()
	{
		Thread t = new Thread() { public void run() { } };
		
		t.setName("ets001 thread");
		
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return check(t);
	}
}
