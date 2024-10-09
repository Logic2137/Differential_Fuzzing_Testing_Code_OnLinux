package com.ibm.j9.offload.tests.jvmservice;





public class Wait1Second {

	
	public static void main(String[] args) {
		try{
			Thread.sleep(1000);
		} catch(InterruptedException e){
			System.out.println("Sleep was interrupted:" + e);
		}
	}

}
