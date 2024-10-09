
package com.ibm.jvmti.tests.redefineClasses;

import java.util.concurrent.Semaphore;

public class rc012_testRedefineRunningNativeMethod_O1 {
	private Semaphore startSem;
	private Semaphore redefineSem;

	public rc012_testRedefineRunningNativeMethod_O1(Semaphore startSem, Semaphore redefineSem) {
		this.startSem = startSem;
		this.redefineSem = redefineSem;
	}

	public native String meth1();

	
	public String meth3() {
		try {
			System.out.println("meth3: release startSem");
			
			startSem.release();
			
			System.out.println("meth3: acquire redefineSem");
			redefineSem.acquire();
			System.out.println("meth3: acquired redefineSem");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String meth2() {
		return "before";
	}

}

