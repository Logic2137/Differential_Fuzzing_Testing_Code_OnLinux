
package org.openj9.test.condy;


public class CondyClassLoader extends ClassLoader  {
	public Class<?> load(String name, byte[] bytes) {
		return (Class<?>) defineClass(name, bytes, 0, bytes.length);
	}
}
