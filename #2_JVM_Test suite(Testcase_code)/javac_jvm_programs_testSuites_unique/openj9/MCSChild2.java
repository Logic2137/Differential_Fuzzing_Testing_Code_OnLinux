
package com.ibm.j9.jsr292;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public class MCSChild2 extends MutableCallSite{

	public MethodHandle ignoredTarget;
	
	public MCSChild2(MethodType type) throws NullPointerException {
		super(type);
	}
	
	@Override
	public void setTarget(MethodHandle nextTarget) {
		ignoredTarget = nextTarget;
	}

}
