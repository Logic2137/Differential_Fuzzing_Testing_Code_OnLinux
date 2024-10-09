
package com.ibm.j9.jsr292.indyn;

public class Helper {
	
	public int publicInt = 1; 
	
	public static int publicStaticInt = 100;

	public static void voidMethod() { }
	
	public static void addStaticPublic(int a, String b){ }
	
	public static int negativeInt(int i) { return -i; }
	
	public static boolean isDivisibleByThree ( int n ) {
		return (n % 3 == 0);
	}
	
	public static boolean isDivisibleByFive ( int n ) {
		return (n % 5 == 0);
	}
	
	public static String returnFizz (int i) {
		return "fizz";
	}
	
	public static String returnBuzz (int i) {
		return "buzz";
	}
	
	public static String returnNothing (int i) {
		return "nothing";
	}
	
	private static String staticPrivateMethod(){
		return "I am a private static method";
	}
}
