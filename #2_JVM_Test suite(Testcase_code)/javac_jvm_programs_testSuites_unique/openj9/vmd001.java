
package com.ibm.jvmti.tests.vmDump;

public class vmd001 
{
	public boolean testVmDump()
	{
		boolean rc = false;
		
		System.out.println("testResetVmDump");
		System.out.println("call tryResetVmDump");
		rc = tryResetVmDump();
		if (rc == true) {
			System.out.println("success [tryResetVmDump]");
		} else {
			System.out.println("error [tryResetVmDump]");
		}
		System.out.println("done testResetVmDump");

		
		System.out.println("testQueryVmDump");
		System.out.println("call tryQueryDumpSmallBuffer");
		rc = tryQueryDumpSmallBuffer();
		if (rc == true) {
			System.out.println("success [tryQueryDumpSmallBuffer]");
		} else {
			System.out.println("error [tryQueryDumpSmallBuffer]");
		}

		System.out.println("call tryQueryDumpBigBuffer");
		if (rc == true) {
			System.out.println("success [tryQueryDumpBigBuffer]");
		} else {
			System.out.println("error [tryQueryDumpBigBuffer]");
		}

		System.out.println("call tryQueryDumpInvalidBufferSize");
		rc = tryQueryDumpInvalidBufferSize();
		if (rc == true) {
			System.out.println("success [tryQueryDumpInvalidBufferSize]");
		} else {
			System.out.println("error [tryQueryDumpInvalidBufferSize]");
		}

		System.out.println("call tryQueryDumpInvalidBuffer");
		rc = tryQueryDumpInvalidBuffer();
		if (rc == true) {
			System.out.println("success [tryQueryDumpInvalidBuffer]");
		} else {
			System.out.println("error [tryQueryDumpInvalidBuffer]");
		}

		System.out.println("call tryQueryDumpInvalidDataSize");
		rc = tryQueryDumpInvalidDataSize();
		if (rc == true) {
			System.out.println("success [tryQueryDumpInvalidDataSize]");
		} else {
			System.out.println("error [tryQueryDumpInvalidDataSize]");
		}

		System.out.println("done testQueryVmDump");

		System.out.println("testSetVmDump");
		System.out.println("call trySetVmDump");
		rc = trySetVmDump();
		if (rc == true) {
			System.out.println("success [trySetVmDump]");
		} else {
			System.out.println("error [trySetVmDump]");
		}
		System.out.println("done testSetVmDump");
		
		System.out.println("testDisableVmDump");
		System.out.println("call tryDisableVmDump");
		rc = tryDisableVmDump();
		if (rc == true) {
			System.out.println("success [tryDisableVmDump]");
		} else {
			System.out.println("error [tryDisableVmDump]");
		}
		System.out.println("done testDisableVmDump");

		return rc;
	}

	public String helpVmDump()
	{
		return "Test the jvmti VmDump extension APIs";
	}

	static public native boolean tryQueryDumpSmallBuffer();
	static public native boolean tryQueryDumpBigBuffer();
	static public native boolean tryQueryDumpInvalidBufferSize();
	static public native boolean tryQueryDumpInvalidBuffer();
	static public native boolean tryQueryDumpInvalidDataSize();

	static public native boolean trySetVmDump();
	static public native boolean tryResetVmDump();
	static public native boolean tryDisableVmDump();
}
