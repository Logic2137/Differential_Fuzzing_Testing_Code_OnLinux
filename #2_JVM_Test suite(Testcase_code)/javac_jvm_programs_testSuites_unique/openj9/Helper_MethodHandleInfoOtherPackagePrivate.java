package org.openj9.test.java.lang.invoke.helpers;



import java.lang.invoke.*;

class Helper_MethodHandleInfoOtherPackagePrivate {
	public static MethodHandles.Lookup lookup() { return MethodHandles.lookup(); }

	private String privateMethod() { return "privateMethod"; }
	protected String protectedMethod() { return "protectedMethod"; }
	String packageMethod() { return "packageMethod"; }
	public String publicMethod() { return "publicMethod"; }
}
