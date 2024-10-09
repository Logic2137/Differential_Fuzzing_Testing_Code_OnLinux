
package com.ibm.dump.tests.javacore_lock_context;


public class EntryRecord {

	private int entryCount;
	private StackTraceElement stackEntry;
	private String lockName;
	
	public EntryRecord( String lockName, int entryCount ) {
		this.entryCount = entryCount;
		this.lockName = lockName.replace('.', '/');;
		
		
		this.stackEntry = (new Throwable()).getStackTrace()[1]; 
	}

	public String getMethodName() {
		String className = stackEntry.getClassName().replace('.', '/');
		return  className + "." +stackEntry.getMethodName();
	}

	public String getLockName() {
		return lockName;
	}

	public int getEntryCount() {
		return entryCount;
	}
	
}
