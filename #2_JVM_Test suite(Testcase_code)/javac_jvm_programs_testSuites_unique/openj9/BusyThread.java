

package org.openj9.test.management.util;


public class BusyThread implements Runnable {
	long busyTime;

	public BusyThread(int mSec) {
		busyTime = mSec * 1000000L;
	}

	public void run() {
		long startTime = System.nanoTime();

		while ((System.nanoTime() - startTime) < busyTime) {
			
		}
	}
}
