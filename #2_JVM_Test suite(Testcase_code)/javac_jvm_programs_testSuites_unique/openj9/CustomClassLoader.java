package org.openj9.test.utilities;



public class CustomClassLoader extends ClassLoader {
	public Class<?> getClass(String name, byte[] bytes){
		return defineClass(name, bytes, 0, bytes.length);
	}
}
