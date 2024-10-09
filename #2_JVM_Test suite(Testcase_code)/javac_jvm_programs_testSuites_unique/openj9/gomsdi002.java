
package com.ibm.jvmti.tests.getOwnedMonitorStackDepthInfo;

public class gomsdi002
{	
	static Object lock = new Object();
	static boolean running = false;
	static volatile boolean stop = false;
	
	native static void callGet(Thread t);
	
	public boolean testForeignThread() throws Throwable
	{
		Thread runner = new Thread() {
			public long recurse(int i, long v) {
				if (i > 0) {
					v = System.currentTimeMillis() + recurse(i - 1, v);
				}
				return v;
			}
			public void run() {
				synchronized(lock) {
					running = true;
					lock.notifyAll();
				}
				while (!stop) {
					recurse(100, 0);
				}
			}
		};		

		runner.start();
		synchronized(lock) {
			while(!running) {
				lock.wait();
			}
		}

		long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < 5000) {
			callGet(runner);
			Thread.sleep(100);
		}

		stop = true;
		runner.join();
		
		return true;
	}
		
	public String helpForeignThread()
	{
		return "test GetOwnedMonitorStackDepthInfo on non-current thread which is running";
	}
}
