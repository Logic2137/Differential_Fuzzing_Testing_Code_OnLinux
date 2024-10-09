
package org.openj9.test.java.lang;

public class InnerClasses {
	public static class InnerStaticClass {
		
	}
	
	public class InnerClass {
		
	}
	
	protected class ProtectedClass {
		
	}
	
	private class PrivateClass {
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getProtectedClass() {
		return ProtectedClass.class;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getPrivateClass() {
		return PrivateClass.class;
	}
}
