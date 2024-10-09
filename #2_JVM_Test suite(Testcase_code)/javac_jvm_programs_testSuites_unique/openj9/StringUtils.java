
package com.ibm.dump.tests;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class StringUtils {

	
	static boolean linePassesCheckForStringAtWord(String line, int whichWord, String requiredValue) {
		StringTokenizer st = new StringTokenizer(line);
		String word = "";
		try {
			for (int i = 0;i < whichWord; i++) {
				word = st.nextToken();
			}
		} catch (NoSuchElementException e) {
			System.err.printf("***Failed to find the value %s at word %d in line \"%s\", no such element\n", requiredValue, whichWord, line, word) ;		
			return false;			
		}
		if (word.equals(requiredValue)) {
			System.err.printf("Successfully found the value %s at word %d in line \"%s\"\n", requiredValue, whichWord, line) ;		
			return true; 
		} else {
			System.err.printf("***Failed to find the value %s at word %d in line \"%s\", found %s instead\n", requiredValue, whichWord, line, word) ;		
			return false;
		}

	}

	
	static boolean linePassesCheckForStringsAtWord(String line, int whichWord, String[] requiredValues) {
		StringTokenizer st = new StringTokenizer(line);
		String word = "";
		try {
			for (int i = 0;i < whichWord; i++) {
				word = st.nextToken();
			}
		} catch (NoSuchElementException e) {
			System.err.printf("***Failed to find any value at word %d in line \"%s\", no such element\n", whichWord, line, word) ;		
			return false;			
		}
		for (String requiredValue : requiredValues) {
			if (word.equals(requiredValue)) {
				System.err.printf("Successfully found the value %s at word %d in line \"%s\"\n", requiredValue, whichWord, line) ;		
				return true; 
			} 
		}
		System.err.printf("***Failed to find any of the required values at word %d in line \"%s\", found %s instead. ", whichWord, line, word) ;
		System.err.printf("The required values were:") ;
		for (String requiredValue : requiredValues) {
			System.err.printf(requiredValue + " ");			
		}
		System.err.printf("\n");			
		return false;
	}


	
	public static ArrayList<String> extractTokensFromLine(String line) {
		ArrayList<String> tokens = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(line);
		while (st.hasMoreTokens()) {
			String word = st.nextToken();
			tokens.add(word);
		}
		return tokens;
	}


}
