
package com.ibm.jvmti.tests.util;


public class AgentHardException extends Exception 
{
	private static final long serialVersionUID = 1L;


	public AgentHardException()
	{
		super();
	}

	public AgentHardException(String message) 
	{
		super(message);
	}
	
	public AgentHardException(Throwable cause) 
	{
		super(cause);
	}
	
	public AgentHardException(String message, Throwable cause) 
	{
		super(message, cause);
	}
	
		
}
