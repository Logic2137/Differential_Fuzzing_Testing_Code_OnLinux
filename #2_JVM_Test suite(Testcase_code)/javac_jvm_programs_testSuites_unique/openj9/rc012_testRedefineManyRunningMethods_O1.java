
package com.ibm.jvmti.tests.redefineClasses;

import java.util.concurrent.Semaphore;

public class rc012_testRedefineManyRunningMethods_O1 {
	private Semaphore sem;

	public rc012_testRedefineManyRunningMethods_O1(Semaphore sem) {
		this.sem = sem;
	}

	private boolean shouldStop() {
		return false;
	}

	private int fib(int n) {
		if (n <= 1) {
			return n;
		} else {
			return fib(n-1)+fib(n-2);
		}
	}

	public String run() {
		sem.release();
		while (!shouldStop()) {
			fib((int)(25*Math.random()));
		}
		return "old";
	}
}

