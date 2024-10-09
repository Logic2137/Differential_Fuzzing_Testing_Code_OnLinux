
package com.ibm.jvmti.tests.redefineClasses;

import java.util.concurrent.Semaphore;

public class rc012_testRedefineRunningMethod_O1 {
	private Semaphore startSem;
	private Semaphore goRecursiveSem;

	public rc012_testRedefineRunningMethod_O1(Semaphore start, Semaphore goRecursive) {
		this.startSem = start;
		this.goRecursiveSem = goRecursive;
	}

	public String doIt(boolean recursive) throws InterruptedException {
		StringBuilder sb = new StringBuilder();
		if (recursive) {
			
			startSem.release();
			
			goRecursiveSem.acquire();
			
			sb.append(doIt(false));
		}
		return sb.toString() + "yes";
	}
}

