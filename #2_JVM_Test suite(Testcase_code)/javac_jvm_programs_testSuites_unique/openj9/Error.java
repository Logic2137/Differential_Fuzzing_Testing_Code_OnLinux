
package com.ibm.jvmti.tests.util;

public class Error extends Throwable
{
	int error;
	String errorName;
	String file;
	String function;
	int	line;
	int type;
	String message;
	
	public final static int JVMTI_TEST_ERR_TYPE_SOFT = 1;
	public final static int JVMTI_TEST_ERR_TYPE_HARD = 2;
	
	
	public native boolean getErrorDetails(int errorIndex);
		
	public String getMessage()
	{
		String msg = "";
			
		msg += "\tError: " + error + "  " + errorName + "\n";		
		msg += "\t\t" + message + "\n";
		msg += "\t\tLocation: " + file + " -> [" + function + "():" + line + "]\n";
				
		return msg;
	}
	
	public boolean isHard()
	{
		return (type == JVMTI_TEST_ERR_TYPE_HARD);
	}
	
}
