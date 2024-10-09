package org.openj9.test.java.security;



import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;


public class ClassLoaderTwo extends ClassLoader {
	private final Set<String> names = new HashSet<>();
	private ClassLoader clOne;
	public ClassLoaderTwo(ClassLoader cl, String... names) {
		clOne = cl;
		for (String name : names) {
			this.names.add(name);
		}
	}
	
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if (name.equals("org.openj9.test.java.security.ChildMHLookupField")) {
			return clOne.loadClass(name);
		}
		if (!names.contains(name)) {
			return super.loadClass(name, resolve);
		}
		Class<?> result = findLoadedClass(name);
		if (result == null) {
			if (name.equals("org.openj9.test.java.security.Parent")) {
				loadClass("org.openj9.test.java.security.Dummy", resolve);
			}
			String filename = name.replace('.', '/') + ".class";
			try (InputStream data = getResourceAsStream(filename)) {
				if (data == null) {
					throw new ClassNotFoundException();
				}
				try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
					int b;
					do {
						b = data.read();
						if (b >= 0) {
							buffer.write(b);
						}
					} while (b >= 0);
					byte[] bytes = buffer.toByteArray();
					result = defineClass(name, bytes, 0, bytes.length);
				}
			} catch (IOException e) {
				throw new ClassNotFoundException("Error reading" + filename, e);
			}
		}
		if (resolve) {
			resolveClass(result);
		}
		return result;
	}
}
