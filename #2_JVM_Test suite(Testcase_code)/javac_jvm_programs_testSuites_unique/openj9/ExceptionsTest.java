

package com.ibm.j9.recreateclass.testclasses;


public class ExceptionsTest {
	public void throwException() throws IllegalArgumentException, InterruptedException {
		Thread.sleep(10);
	}
}
