
package com.ibm.jvmti.tests.redefineClasses;

import java.util.concurrent.Semaphore;

public class rc012_testRedefineRunningNativeMethod_R1 {
	private Semaphore startSem;
	private Semaphore redefineSem;

	public rc012_testRedefineRunningNativeMethod_R1(Semaphore startSem, Semaphore redefineSem) {
		this.startSem = startSem;
		this.redefineSem = redefineSem;
	}

	public native String meth1();

	
	public String meth3() {
		try {
			
			startSem.release();
			
			redefineSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String meth2() {
		return "after";
	}
}

