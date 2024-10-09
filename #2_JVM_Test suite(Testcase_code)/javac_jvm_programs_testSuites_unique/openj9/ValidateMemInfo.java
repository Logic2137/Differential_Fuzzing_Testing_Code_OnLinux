
package com.ibm.dump.tests.javacore_meminfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ValidateMemInfo {

	
	public static void main(String[] args) {

		if (args.length < 2) {
			System.err.println("Input javacore and jdmpview !dumpallsegments files are required.");
			System.exit(1);
		}
		String javacoreFile = args[0];
		String jdmpviewFile = args[1];

		BufferedReader in1 = null;
		BufferedReader in2 = null;
		try {
			in1 = new BufferedReader(new FileReader(javacoreFile));
			in2 = new BufferedReader(new FileReader(jdmpviewFile));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
			System.exit(1);
		}
		if (in1 == null) {
			System.err.println("Failed to open javacore file");
			System.exit(1);
		}
		if (in2 == null) {
			System.err.println("Failed to open jdmpview !dumpallsegments file");
			System.exit(1);
		}

		boolean rc = compareMemInfo(in1, in2);

		if (rc == true) {
			System.err.println("Test passed.");
			System.exit(0);
		} else {
			System.err.println("Test failed.");
			System.exit(1);
		}
	}
	

	
	private static boolean compareMemInfo(BufferedReader in1, BufferedReader in2) {
		String line;
		
		
		long jcCodeCacheTotal = 0;
		long jcCodeCacheAlloc = 0;
		long jcCodeCacheFree = 0;
		
		
		try {
			System.out.println("Parsing code cache memory segments in javacore:");
			line = in1.readLine();
			
			while (line != null && !line.startsWith("1STSEGTYPE     JIT Code Cache")) {
				line = in1.readLine();
			}
			
			while ((line = in1.readLine()) != null ) {
				if (line.startsWith("1STSEGMENT")) {
					String[] values = line.split("[ ]+");
					
					long start = Long.parseLong(values[2].substring(2), 16);
					long alloc = Long.parseLong(values[3].substring(2), 16);
					long end = Long.parseLong(values[4].substring(2), 16);
					
					jcCodeCacheTotal += end - start; 
					jcCodeCacheAlloc += alloc - start;
					jcCodeCacheFree += end - alloc;
					continue; 
				} else if (line.startsWith("1STSEGTOTAL")) {
					String[] values = line.split("[ ]+");
					if (jcCodeCacheTotal == Long.parseLong(values[3], 10)) {
						System.out.println("\tcorrect total");
					} else {
						System.out.println("\tincorrect total: " + jcCodeCacheTotal + " " + Long.parseLong(values[3], 10));
					}
					continue;
				} else if (line.startsWith("1STSEGINUSE")) {
					String[] values = line.split("[ ]+");
					if (jcCodeCacheAlloc == Long.parseLong(values[5], 10)) {
						System.out.println("\tcorrect in use total");
					} else {
						System.out.println("\tincorrect in use total: " + jcCodeCacheAlloc + " " + Long.parseLong(values[5], 10));
					}
					continue;
				} else if (line.startsWith("1STSEGFREE")) {
					String[] values = line.split("[ ]+");
					if (jcCodeCacheFree == Long.parseLong(values[4], 10)) {
						System.out.println("\tcorrect free total");
					} else {
						System.out.println("\tincorrect free total: " + jcCodeCacheFree + " " + Long.parseLong(values[4], 10));
					}
					break; 
				} else if (line.startsWith("NULL")) {
					
				} else {
					System.err.println("Unexpected javacore line");
					break;
				}
 			}
		} catch (IOException e) {
			System.err.println("Error reading javacore file.");
			return false;
		}

		
		long jdCodeCacheTotal = 0;
		long jdCodeCacheAlloc = 0;
		long jdCodeCacheFree = 0;
		
		try {
			System.out.println("Parsing code cache memory segments in jdmpview output:");
			line = in2.readLine();
			
			while (line != null && !line.startsWith("jit code segments")) {
				line = in2.readLine();
			}
			
			while ((line = in2.readLine()) != null ) {
				if (line.startsWith("Total memory:")) {
					String[] values = line.split("[ ]+");
					if (jdCodeCacheTotal == Long.parseLong(values[2], 10)) {
						System.out.println("\tcorrect total");
					} else {
						System.out.println("\tincorrect total: " + jdCodeCacheTotal + " " + Long.parseLong(values[2], 10));
					}
					continue;
				} else if (line.startsWith("Total memory in use:")) {
					String[] values = line.split("[ ]+");
					if (jdCodeCacheAlloc == Long.parseLong(values[4], 10)) {
						System.out.println("\tcorrect in use total");
					} else {
						System.out.println("\tincorrect in use total: " + jdCodeCacheAlloc + " " + Long.parseLong(values[4], 10));
					}
					continue;
				} else if (line.startsWith("Total memory free:")) {
					String[] values = line.split("[ ]+");
					if (jdCodeCacheFree == Long.parseLong(values[3], 10)) {
						System.out.println("\tcorrect free total");
					} else {
						System.out.println("\tincorrect free total: " + jdCodeCacheFree + " " + Long.parseLong(values[3], 10));
					}
					break; 
				} else if (line.startsWith("+")) {
					
				} else if (line.startsWith("|")) {
					
				} else {
					
					String[] values = line.split("[ ]+");
					long start = Long.parseLong(values[2], 16);
					long warm = Long.parseLong(values[3], 16);
					long cold = Long.parseLong(values[4], 16);
					long end = Long.parseLong(values[5], 16);
					System.out.println("\tsegment values " + start + " " + warm + " " + cold + " " + end);
					jdCodeCacheTotal += end - start; 
					jdCodeCacheAlloc += (warm - start) + (end - cold);
					jdCodeCacheFree += cold - warm;
					continue; 
				}
 			}
		} catch (IOException e) {
			System.err.println("Error reading jdmpview output file.");
			return false;
		}
		
		
		System.out.println("Code cache (javacore) total: " + jcCodeCacheTotal + " alloc: " + jcCodeCacheAlloc + " free: " + jcCodeCacheFree);
		System.out.println("Code cache (coredump) total: " + jdCodeCacheTotal + " alloc: " + jdCodeCacheAlloc + " free: " + jdCodeCacheFree);
		if (jcCodeCacheTotal != jdCodeCacheTotal) {
			System.err.println("Test failed: code cache total memory validation failed");
			return false;
		}
		
		
		if (Math.abs(jcCodeCacheAlloc - jdCodeCacheAlloc) > jcCodeCacheTotal/10) {
			System.err.println("Test failed: code cache allocated memory validation failed");
			return false;
		}
		if (Math.abs(jcCodeCacheFree - jdCodeCacheFree) > jcCodeCacheTotal/10) {
			System.err.println("Test failed: code cache free memory validation failed");
			return false;
		}
		return true;
	}

}
