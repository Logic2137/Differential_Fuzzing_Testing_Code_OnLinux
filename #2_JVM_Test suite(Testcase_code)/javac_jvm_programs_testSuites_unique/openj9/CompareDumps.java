
package com.ibm.dump.tests.jdmpview_heapdump;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CompareDumps {

	public static void main(String[] args) throws IOException {
		
		int error_count = 0;
		if (args.length != 2) {
			System.err.println("CreateDump takes two arguments: the names of two files to compare");
		}
		
		System.err.println("Comparing the first 1000 lines of " + args[0] + " with the first 1000 lines of " + args[1]);
		BufferedReader in1 = new BufferedReader(new FileReader(args[0]));
		BufferedReader in2 = new BufferedReader(new FileReader(args[1]));
		
		for (int i = 0;i<1000;i++) {
			String line1 = in1.readLine();
			String line2 = in2.readLine();
			if ( ! line1.equals(line2)) {
				error_count++;
				System.err.println("The two files differ at line " + (i + 1));
				System.err.println("The first file has:");
				System.err.println(line1);
				System.err.println("The second file has:");
				System.err.println(line2);
			}
		}
		
		System.err.println("Count of differences in the first 1000 lines: " + error_count);
		
		System.exit(error_count);
}

}
