package org.openj9.test.java.security;



import java.net.URL;
import java.net.URLClassLoader;


public class TestURLClassLoader extends URLClassLoader {

	public TestURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> result;
		if (name.startsWith("org.testng")) { 
			result = Class.forName(name, true, ClassLoader.getSystemClassLoader());
		} else {
			result = super.findClass(name);
		}
		return result;
	}
}
