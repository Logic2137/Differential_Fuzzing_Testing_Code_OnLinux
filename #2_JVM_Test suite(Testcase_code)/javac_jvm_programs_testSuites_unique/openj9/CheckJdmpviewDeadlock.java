
package com.ibm.dump.tests.javacore_deadlock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CheckJdmpviewDeadlock {

	
	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("An input file is required to check.");
			System.exit(1);
		}
		String fileName = args[0];

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("File: " + fileName + " not found.");
			System.exit(1);
		}
		if (in == null) {
			System.err.println("Failed to open file: " + fileName);
			System.exit(1);
		}

		boolean passed = true;

		passed &= checkDeadlockSection(in);

		if( passed ) {
			System.err.println("Test passed.");
			System.exit(0);
		} else {
			System.err.println("Test failed.");
			System.exit(1);
		}
	}
	
	private static boolean checkDeadlockSection(BufferedReader in) {
		String line;
		boolean passed = true;
		int deadlockCount = 0;
		final int expectedDeadlocks = 3;
		
		
		
		try {
			line = readLine(in);
			while( line != null ) {
				while (line != null && !line.startsWith("deadlock loop:") && deadlockCount < expectedDeadlocks) {
					line = readLine(in);
				}
				if( line == null ) {
					System.err.println("Null line");
					passed = false;
					break;
				}
				if( deadlockCount == expectedDeadlocks ) {
					break;
				}
				deadlockCount++;
				System.err.println("Found a deadlock, line: " + line);
				line = readLine(in);
				
				List<Integer> threadNums = new ArrayList<Integer>();
				List<String> threadNames = new ArrayList<String>();
				while(true) {
					if( line.startsWith("thread: ") ) {
						String threadName = line.substring(line.indexOf("thread: ") + "thread: ".length(), line.indexOf("id: "));
						int threadNo = getThreadNumber(threadName);
						threadNames.add(threadName);
						threadNums.add(threadNo);
					} else {
						break; 
					}
					line = readLine(in);
				}
				
				
				int currentThread = threadNums.get(0);
				int firstThread = currentThread; 
				String currentName = threadNames.get(0);
				String firstName = currentName;
				String threadPrefix = currentName.substring( 0, currentName.indexOf("##")).trim();
				
				System.err.println("Checking deadlock chain for \"" + threadPrefix + "\" threads that is " + (threadNums.size() - 1) + " threads long");
				for( int i = 1; i < threadNums.size(); i++ ) {
					int nextThread = threadNums.get(i);
					String nextThreadName = threadNames.get(i);
					if( nextThread != currentThread + 1 && nextThread != 0 ) {
						System.err.println("Threads out of order: " + threadNames.get(i) + " after " + currentThread );
						passed &= false;
						break;
					}
					if( !nextThreadName.startsWith(threadPrefix)) {
						System.err.println("Wrong thread type: " + threadNames.get(i) + " does not start with " + currentThread );
						passed &= false;
						break;
					}
					currentThread = nextThread;
					currentName = nextThreadName;
				}
				if( firstThread == currentThread ) {
					System.err.println("Complete thread cycle found, confirmed deadlock for " + threadPrefix + " threads" );
				}
				System.err.println("------------");
				
			}
		} catch (IOException e) {
			System.err.println("Error reading javacore file.");
			System.exit(1);
		}
		if( deadlockCount == expectedDeadlocks ) {
			passed &= true;
		}
		
		return passed;
	}

	
	private static String readLine(BufferedReader in) throws IOException {
		String line = in.readLine();
		return line!=null?line.trim():null;
	}

	public static int getThreadNumber(String threadName) {
		
		
		int openQuote = -1;
		int closeQuote = -1;
		openQuote = threadName.indexOf("##");
		closeQuote = threadName.indexOf("##", openQuote + 1);
		if( openQuote > -1 && closeQuote > -1 ) {
			String threadNo_string = threadName.substring(openQuote + 2, closeQuote);
			int threadNo = Integer.parseInt(threadNo_string);
			return threadNo;
		} else {
			return Integer.MIN_VALUE;
		}
	}
}
