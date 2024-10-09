package com.ibm.j9.offload.tests.jvmservice;





public class ExitWithReturnCode78 {

	
	public static void main(String[] args) {
		try{
			Thread.sleep(1000);
			System.exit(78);
		} catch(InterruptedException e){
			System.out.println("Sleep was interrupted:" + e);
		}
	}

}
