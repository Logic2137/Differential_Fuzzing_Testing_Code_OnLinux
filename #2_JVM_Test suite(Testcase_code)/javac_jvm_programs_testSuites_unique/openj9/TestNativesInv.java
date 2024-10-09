package com.ibm.j9.offload.tests.invcost;



public class TestNativesInv {
	static final String NATIVE_LIBRARY_NAME = "j9offjnitest26";
	static boolean libraryLoaded = false;
	
	
	public int suma, sumb, sumc, sumd, sume, sumf;

	
	public native int doNothing();
	public native int doNothing2(Object obj1, Object obj2);
	public native int doNothing3(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5);
	public native int doCallback();
	public native int sumValues(int a, int b, int c, int d, int e, int f);
	public native int sumValues2();
	public static native long getElement(long[] array,int element);
	public static native long getElement2(long[] array,int element);
	
	public int doNothingInJava(){
		return 1;
	}
	
	public int doNothingInJava2(Object obj1, Object obj2){
		return 1;
	}
	
	public int doNothingInJava3(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5){
		return 1;
	}
	
	
	public interface testInterface {
		public void method1();
	}
	
	public class testClass {
		public void method1() {};
	}
	
	
	public void ensureLibraryLoaded(){
		if (libraryLoaded == false){
			System.loadLibrary(NATIVE_LIBRARY_NAME);
			libraryLoaded = true;
		}
	}
	
		
	public TestNativesInv() {
		ensureLibraryLoaded();
	}	

	
	public TestNativesInv(int intValue){
		ensureLibraryLoaded();
	}
}
