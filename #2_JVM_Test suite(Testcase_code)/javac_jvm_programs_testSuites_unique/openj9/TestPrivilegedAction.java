
package org.openj9.test.java.security;

import java.security.PrivilegedAction;

public class TestPrivilegedAction implements PrivilegedAction {
	public Object run() {
		System.getProperty("test.property1");
		return null;
	}
}
