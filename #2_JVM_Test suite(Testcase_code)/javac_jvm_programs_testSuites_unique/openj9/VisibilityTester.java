
package org.openj9.test.modularity.common;

public interface VisibilityTester {

	void testFromMethodDesc();
	void testClassForNameAndFindVirtual();
	void testFindClass();
	void testLoadMethodHandle();
	void testFindStatic();
}
