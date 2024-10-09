package com.ibm.j9.offload.tests.library;



public class LibraryNatives {
	public static final String NATIVE_LIBRARY_NAME = "j9offjnitest26";
	public static native int setPrintfOnUnload();
	public static native int getOnLoadCalled();
	
	static boolean libraryLoaded = false;
	
	
	public void ensureLibraryLoaded(){
		if (libraryLoaded == false){
			System.loadLibrary(NATIVE_LIBRARY_NAME);
			libraryLoaded = true;
		}
	}
	
	public LibraryNatives(){
		ensureLibraryLoaded();
		setPrintfOnUnload();
	}
}


