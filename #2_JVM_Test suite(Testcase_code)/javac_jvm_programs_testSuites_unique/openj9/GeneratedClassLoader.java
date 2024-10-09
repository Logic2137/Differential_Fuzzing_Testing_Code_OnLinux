
package org.openj9.test.invoker.util;

public class GeneratedClassLoader extends ClassLoader {
	private byte[] classData;

	public GeneratedClassLoader() {
	}

	public void setClassData(byte[] classData) {
		this.classData = classData;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return defineClass(name, classData, 0, classData.length);
		} catch (Exception e) {
			throw new ClassNotFoundException("Failed to generate class data for " + name, e);
		}
	}
}
