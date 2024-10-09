
package org.openj9.test.java.security;

import java.security.PrivilegedExceptionAction;

public class TestPrivilegedExceptionAction implements PrivilegedExceptionAction {
	public Object run() throws Exception {
		System.getProperty("test.property2");
		return null;
	}
}
