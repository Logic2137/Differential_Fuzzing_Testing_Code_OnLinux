
package com.ibm.j9.security;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.DomainCombiner;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.SecurityPermission;
import java.util.PropertyPermission;

public class PR66930ACCcheckPermission {

	private static boolean	untrustedPDimpliesCalled = false;
	private final static PropertyPermission PERM_JAVA_VERSION_READ = new PropertyPermission("java.version", "read");
	private final static SecurityPermission PERM_CREATE_ACC = new SecurityPermission("createAccessControlContext");

	public static void main(String[] args) {
		new PR66930ACCcheckPermission().test();
	}

	void test() {
		final String	baseName = this.getClass().getName();
		ClassLoader	privilegedCL = new URLClassLoader(new URL[]{this.getClass().getProtectionDomain().getCodeSource().getLocation()}, null) {
			public PermissionCollection getPermissions(CodeSource cs) {
				PermissionCollection pc = super.getPermissions(cs);
				pc.add(PERM_CREATE_ACC);
				return pc;
			}
		};
		
		try {
			Class<?>	cls = Class.forName(baseName + "$PrivilegedClass", true, privilegedCL);
			Object	obj = cls.newInstance();
			Method	mt = cls.getMethod("test", AccessControlContext.class);
			ProtectionDomain pd1 = new ProtectionDomain(null, null) {
				public boolean implies(Permission perm) {
					untrustedPDimpliesCalled = true;
					System.out.println("Untrusted ProtectionDomain.implies() has been called");
					return true;
				}
			};

			System.setSecurityManager(new SecurityManager());
			
			AccessControlContext	accSimple = new AccessControlContext(new ProtectionDomain[]{pd1});
			untrustedPDimpliesCalled = false;
			accSimple.checkPermission(PERM_JAVA_VERSION_READ);
			if (!untrustedPDimpliesCalled) {
				System.out.println("FAILED: untrusted ProtectionDomain.implies() should have been called");
			}
			
			AccessControlContext	accInjected = AccessController.doPrivileged(new PrivilegedAction<AccessControlContext>() {
				public AccessControlContext run() {
					return AccessController.getContext();
				}
			}, accSimple);
			untrustedPDimpliesCalled = false;
			try {
				accInjected.checkPermission(PERM_JAVA_VERSION_READ);
				System.out.println("FAILED: AccessControlException NOT thrown");
			} catch (AccessControlException ace) {
				System.out.println("GOOD: AccessControlException is expected");
			}                                   
			if (untrustedPDimpliesCalled) {
				System.out.println("FAILED: untrusted ProtectionDomain.implies() should NOT be called");
			}
			
			mt.invoke(obj, accInjected);
			
			System.out.println("ALL TESTS FINISHED");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAIL: TEST FAILED, probably setup issue.");
		}
	}
	
	public static class PrivilegedClass {
		public void test(final AccessControlContext accIncoming) {
			
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				public Void run() {
					try {
						
						new AccessControlContext(accIncoming, new DomainCombiner() {
							public ProtectionDomain[] combine(ProtectionDomain[] arg0, ProtectionDomain[] arg1) {
								return null;
							}
						});
						AccessController.checkPermission(PERM_CREATE_ACC);
						AccessController.getContext().checkPermission(PERM_JAVA_VERSION_READ);
					} catch (AccessControlException ace) {
						System.out.println("FAILED: AccessControlException is NOT expected");
					}
					try {
						
						accIncoming.checkPermission(PERM_JAVA_VERSION_READ);
						System.out.println("FAILED: AccessControlException NOT thrown");
					} catch (AccessControlException ace) {
						System.out.println("GOOD: AccessControlException is expected");
					}
					return null;
				}
			});
			
			
			try {
				new AccessControlContext(accIncoming, new DomainCombiner() {
					public ProtectionDomain[] combine(ProtectionDomain[] arg0, ProtectionDomain[] arg1) {
						return null;
					}
				});
				System.out.println("FAILED: AccessControlException NOT thrown");
			} catch (AccessControlException ace) {
				System.out.println("GOOD: AccessControlException is expected");
			}
			try {
				AccessController.checkPermission(PERM_CREATE_ACC);
				System.out.println("FAILED: AccessControlException NOT thrown");
			} catch (AccessControlException ace) {
				System.out.println("GOOD: AccessControlException is expected");
			}
			try {
				
				accIncoming.checkPermission(PERM_JAVA_VERSION_READ);
				System.out.println("FAILED: AccessControlException NOT thrown");
			} catch (AccessControlException ace) {
				System.out.println("GOOD: AccessControlException is expected");
			}
		}
	}
}
