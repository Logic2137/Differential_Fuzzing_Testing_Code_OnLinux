
package com.ibm.j9.security.bootpath;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class CodeTrusted {
	public String getPropertyPrivDC(final String prop) {
		return AccessController.doPrivilegedWithCombiner(new PrivilegedAction<String>() {
			public String run() {
				System.out.println("Codetrusted - getPropertyPrivDC prop = " + prop);
				return System.getProperty(prop);
			}
		});
	}
}
