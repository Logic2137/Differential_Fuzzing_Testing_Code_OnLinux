

package com.ibm.j9.recreateclass.testclasses;

public class JSRRETStressTestLoader extends ClassLoader {
	byte[] classData;
	public JSRRETStressTestLoader(byte[] classData) {
		super();
		this.classData = classData;
	}
	
	public Class<?> findClass(String name) {
		return defineClass(name, classData, 0, classData.length);
	}
}
