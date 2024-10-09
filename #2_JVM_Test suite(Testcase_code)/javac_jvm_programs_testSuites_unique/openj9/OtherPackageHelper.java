
package org.openj9.test.varhandle.otherpkg;

public class OtherPackageHelper {
	public static int publicStaticInt = 321;
	private static int privateStaticInt = 654;
	
	int packageInt;
	public int publicInt;
	private int privateInt;
	protected int protectedInt;
	
	public OtherPackageHelper() {
		packageInt = 123;
		publicInt = 234;
		privateInt = 345;
		protectedInt = 456;
	}
}
