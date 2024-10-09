package com.ibm.j9.offload.tests.unsafemem;
import java.nio.ByteBuffer;



public class TestNativesUnsafe  {
	
	static final String NATIVE_LIBRARY_NAME = "j9offjnitest26";
	static boolean libraryLoaded = false;
	
	
	public void ensureLibraryLoaded(){
		if (libraryLoaded == false){
			System.loadLibrary(NATIVE_LIBRARY_NAME);
			libraryLoaded = true;
		}
	}
	
		
	public TestNativesUnsafe() {
		ensureLibraryLoaded();
	}	
	
	public native ByteBuffer getNativeAllocatedByteBuffer(int size);	

}
