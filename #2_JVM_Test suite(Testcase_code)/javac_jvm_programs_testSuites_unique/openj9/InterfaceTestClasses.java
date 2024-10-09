package org.openj9.test.java.lang.specimens;



public abstract class InterfaceTestClasses{

	public interface I1 {
		default void m1() {
		}
		default void m2() {
		}
		default void m3() {
		}
	}

	public interface I2 extends I1 {
		void m1();
		void m2();
		void m3();
	}
	public interface I3 extends I1, I2 {
		void m1();
		void m2();
		void m3();
	}
	public abstract class SuperDuperClass implements I1, I2, I3{}
	public abstract class SuperClass extends SuperDuperClass implements I1, I2, I3{}
	public abstract class SubClass extends SuperClass implements I1, I2, I3 {}

}
