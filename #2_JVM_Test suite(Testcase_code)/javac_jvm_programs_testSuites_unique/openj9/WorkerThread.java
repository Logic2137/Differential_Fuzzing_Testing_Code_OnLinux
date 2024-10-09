

package org.openj9.test.management.util;

import java.util.concurrent.CountDownLatch;


public class WorkerThread implements Runnable {
	long busyTime;
	private final CountDownLatch startLatch;
	private final CountDownLatch joinLatch;

	public WorkerThread(CountDownLatch startLatch, CountDownLatch joinLatch, int mSec) {
		busyTime = mSec * 1000000L;
		this.startLatch = startLatch;
		this.joinLatch = joinLatch;
	}

	public void run() {
		try {
			startLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long startTime = System.nanoTime();
		while ((System.nanoTime() - startTime) < busyTime) {
			
			for (long i = 1; i < 1000000; i++) {
				quadratic(1, 2, 3, i);
			}
		}
		joinLatch.countDown();
	}

	public static long quadratic(long coeffA, long coeffB, long coeffC, long number) {
		return (coeffA * (number * number) + coeffB * number + coeffC);
	}

}
