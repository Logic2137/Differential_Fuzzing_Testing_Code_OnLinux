

package org.openj9.test.assertionstatus;



class SubClassLoader extends ClassLoader {
	
	class InnerClassLoader extends SubClassLoader {
		private boolean setDefaultAssertionStatusMethodCalled;
		private boolean setPackageAssertionStatusMethodCalled;
		private boolean setClassAssertionStatusMethodCalled;
		
		public boolean isAssertionStatusPublicMethodCalled() {
			return (setDefaultAssertionStatusMethodCalled || setPackageAssertionStatusMethodCalled || setClassAssertionStatusMethodCalled);
		}
		
		public void setDefaultAssertionStatus(boolean enable) {
			setDefaultAssertionStatusMethodCalled = true;
			new Throwable().printStackTrace();
		}
		
		public void setPackageAssertionStatus(String pname, boolean enable) {
			setPackageAssertionStatusMethodCalled = true;
			new Throwable().printStackTrace();
		}
		
		public void setClassAssertionStatus(String cname, boolean enable) {
			setClassAssertionStatusMethodCalled = true;
			new Throwable().printStackTrace();
		}
	}
}

public class EnableAssertionStatusTest {
	
	public static void main(String[] args) throws Throwable {
		SubClassLoader subCldr = new SubClassLoader();
		SubClassLoader.InnerClassLoader innerCldr = subCldr.new InnerClassLoader();
		
		if (innerCldr.isAssertionStatusPublicMethodCalled()) {
			System.out.println("EnableAssertionStatusTest FAILED");
		} else {
			System.out.println("EnableAssertionStatusTest PASSED");
		}
	}
}
