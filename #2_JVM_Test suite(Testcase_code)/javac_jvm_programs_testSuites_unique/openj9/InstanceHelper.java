
package org.openj9.test.varhandle;

public class InstanceHelper {
	byte b;
	char c;
	double d;
	float f;
	int i;
	long j;
	String l;
	short s;
	boolean z;
	Integer integer;
	Parent parent = new Parent();
	Child child = new Child();
	final int finalI = 1;

	InstanceHelper() {}
	InstanceHelper(byte b) { this.b = b; }
	InstanceHelper(char c) { this.c = c; }
	InstanceHelper(double d) { this.d = d; }
	InstanceHelper(float f) { this.f = f; }
	InstanceHelper(int i) { this.i = i; }
	InstanceHelper(long j) { this.j = j; }
	InstanceHelper(short s) { this.s = s; }
	InstanceHelper(boolean z) { this.z = z; }
	InstanceHelper(String l) { this.l = l; }
	InstanceHelper(Integer i) { this.integer = i; }
}

class Parent {
	int i;
}

class Child extends Parent {}

class SomeType {}
