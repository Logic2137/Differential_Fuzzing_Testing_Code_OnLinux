
package com.ibm.j9.jsr292;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public class MCSChild1 extends MutableCallSite{

	public MCSChild1(MethodType type) throws NullPointerException {
		super(type);
	}
	
	@Override
	public void setTarget(MethodHandle nextTarget) {
		super.setTarget(nextTarget);
	}

}
