
package org.openj9.test.NoSuchMethod;

class ClassInfo {
	private Class<?> clazz;

	public ClassInfo(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getName() {
		return clazz != null ? clazz.getName() : null;
	}

	public String toString() {
		return clazz != null ? clazz.toString() : null;
	}

	public String getClassLoader() {
		if (clazz == null)
			return null;
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader != null) {
			return classLoader.toString();
		} else {
			return "<Bootstrap Loader>";
		}
	}

	public String getClassPath() {
		if (clazz == null)
			return null;
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader == null) {
			return null;
		}
		try {
			return clazz.getProtectionDomain().getCodeSource().getLocation().toString();
		} catch (Exception e) {
			return "<Unknown>";
		}
	}

}
