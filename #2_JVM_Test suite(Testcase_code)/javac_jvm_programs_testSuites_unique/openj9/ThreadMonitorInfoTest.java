
package com.ibm.jvmti.tests.getOwnedMonitorInfo;

public class ThreadMonitorInfoTest extends Thread 
{
	
	Object monitorA = new Object();
	Object monitorB = new Object();
		
	native static boolean verifyMonitors(Thread t, Object monitorA, Object monitorB);
	
	private int monitorCount = 0;
	
	public ThreadMonitorInfoTest(int monitorCount)
	{
		this.monitorCount = monitorCount;
	}

	
	public void run()
	{	
		if (monitorCount == 1) {
			synchronized(monitorA) {
				verifyMonitors(this, monitorA, null);
			}
		} else if (monitorCount == 2) {
			synchronized(monitorA) {
				synchronized(monitorB) {
					verifyMonitors(this, monitorA, monitorB);
				}
			}			
		}
	}
}
