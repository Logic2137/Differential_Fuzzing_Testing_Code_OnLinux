

package org.openj9.test.management.util;


public class ChildWatchdog extends Thread {
	Process child;
	int killTimeout;

	public ChildWatchdog(Process child, int timeout) {
		super();
		this.child = child;
		this.killTimeout = timeout;
	}

	@Override
	public void run() {
		try {
			sleep(killTimeout);
			child.destroy();
			System.err.println("Child Hung");
		} catch (InterruptedException e) {
			return;
		}
	}
}
