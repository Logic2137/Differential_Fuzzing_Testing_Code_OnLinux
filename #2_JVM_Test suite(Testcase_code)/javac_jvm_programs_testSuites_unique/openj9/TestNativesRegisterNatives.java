package com.ibm.j9.offload.tests.jniservice;



public class TestNativesRegisterNatives {
	static final String NATIVE_LIBRARY_NAME1 = "j9offjnitest26";
	static final String NATIVE_LIBRARY_NAME2 = "j9offjnitestb26";
	static boolean librariesLoaded = false;
	
	
	public void ensureLibraryLoaded(){
		if (librariesLoaded == false){
			System.loadLibrary(NATIVE_LIBRARY_NAME1);
			System.loadLibrary(NATIVE_LIBRARY_NAME2);
			librariesLoaded = true;
		}
	}
	
		
	public TestNativesRegisterNatives() {
		ensureLibraryLoaded();
	}	
	
	
	
	public int nonNative(){
		return 10;
	}
	
	
	
	public native int unregisterNatives();
	public native int registerWithInvalidMethodName();
	public native int registerWithNonNativeMethodName();
	
	public native int registerNativesReturn1ValueInLibrary1();
	public native int registerNativesReturn1ValueInLibrary2();
	public native int registerNativesReturn1ValueInLibrary2SeparateThread();
	
	public native int registerNativesReturn2ValueInLibrary1();
	public native int registerNativesReturn2ValueInLibrary2();
	public native int registerNativesReturn2ValueInLibrary2SeparateThread();
	
	public native int getValueRegisteredNativeLibrary1();
	public native int getValueRegisteredNativeLibrary2();
	
	public native int getValueRegisteredNativeLibrary1NoDefault();
	public native int getValueRegisteredNativeLibrary2NoDefault();
	
	
}
