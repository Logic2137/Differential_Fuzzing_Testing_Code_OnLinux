
package com.ibm.j9.security;

import java.security.AccessControlException;
import java.security.AccessController;
import java.util.PropertyPermission;

public class TestToArrayOfProtectionDomains {
	public static void main(String[] args) {
		try {
			AccessController.checkPermission(new PropertyPermission("java.home", "read"));
			System.out.println("FAILED: access not denied!");
		} catch (AccessControlException ace) {
			System.out.println("ALL TESTS COMPLETED AND PASSED");
		}
	}
}
