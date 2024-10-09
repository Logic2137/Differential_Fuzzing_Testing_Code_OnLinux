
package com.ibm.jvmti.tests.BCIWithASM;

import java.io.FileOutputStream;

public class Source13 {
	
	public void method1(){
		System.out.println("Source13#method1() : entering sleep-awake cycle..");
		for ( int i = 0 ; i < 10 ; i++ ) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
		method3();
	}

	public void method2(){
		System.out.println("Source13#method2() : Performing array copy..");
		double counter = 0 ;
		for ( int i = 5 ; i < 10 ; i++ ) {
			counter += Math.random();
			System.arraycopy(new String[]{"1","2","3"}, 0, new Object[]{"4","5","6"}, 1, 2);
		}
		System.out.println("Done!");
	}

	public void method3() {
		try {
			System.out.println("Source13#method3() : Performing Garbage dump");
			FileOutputStream fos = new FileOutputStream( "garbage_dump" );
			fos.write(new byte[]{1,2,3,4,5,6,7} );
			fos.flush();
			fos.close();
			System.out.println("Done!");
			method2();
		} catch (Exception e ) {}
		
	}
}
