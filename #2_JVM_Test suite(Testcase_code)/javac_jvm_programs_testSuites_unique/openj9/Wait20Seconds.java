package com.ibm.j9.offload.tests.jvmservice;




public class Wait20Seconds {

	
	public static void main(String[] args) {
		try{
			Thread.sleep(20000);
		} catch(InterruptedException e){
			System.out.println("Sleep was interrupted:" + e);
		}
	}
}
