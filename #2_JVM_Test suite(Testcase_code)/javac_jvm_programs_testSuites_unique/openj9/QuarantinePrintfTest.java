package com.ibm.j9.offload.tests;




public class QuarantinePrintfTest {
	static final String NATIVE_LIBRARY_NAME = "j9offjnitest26";
	public static native int testPrintfOutput(int value);
	
	public static void main(String[] args){
		System.loadLibrary(NATIVE_LIBRARY_NAME);
		testPrintfOutput(0);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}


