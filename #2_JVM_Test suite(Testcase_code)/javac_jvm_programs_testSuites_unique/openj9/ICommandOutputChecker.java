
package com.ibm.dump.tests.commands;

import java.util.List;


public interface ICommandOutputChecker {
	
	
	public boolean check(String command, String args, List<String> outputLines);
}
