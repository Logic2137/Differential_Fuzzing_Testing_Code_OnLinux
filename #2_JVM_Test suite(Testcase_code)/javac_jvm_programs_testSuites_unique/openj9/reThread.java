
package com.ibm.jvmti.tests.resourceExhausted;

public class reThread extends Thread 
{
	Object m;
	
	public reThread(Object lock)
	{
		m = lock;
	}
	
	public synchronized void run()
	{		
		synchronized(m) 
		{
		
			try {
				m.wait();
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}		
		}
	}
	
}
