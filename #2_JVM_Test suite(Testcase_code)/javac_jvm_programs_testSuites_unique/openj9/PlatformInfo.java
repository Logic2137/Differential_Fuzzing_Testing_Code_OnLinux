
package org.openj9.test.util;

public class PlatformInfo {
	private static final String OS_NAME = "os.name"; 
	private static final String osName = System.getProperty(OS_NAME);
	private static final boolean isPlatformZOS = (osName != null) && osName.toLowerCase().startsWith("z/os"); 
	private	static final boolean isOpenJ9Status = 
			System.getProperty("java.vm.vendor").contains("OpenJ9"); 

	public static boolean isWindows() {
		return ((null != osName) && osName.startsWith("Windows"));
	}

	public static boolean isMacOS() {
		return ((null != osName) && osName.startsWith("Mac OS"));
	}

	public static boolean isZOS() {
		return isPlatformZOS;
	}

	
	public static boolean isOpenJ9() {
		return isOpenJ9Status;
	}

	public static String getLibrarySuffix() {
		String librarySuffix = ".so"; 
		if (isWindows()) {
			librarySuffix = ".dll";
		} else if (isMacOS()) {
			librarySuffix = ".dylib";
		}
		return librarySuffix;
	}

}
