

package org.openj9.test.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class StringUtilities {

	
	public static Optional<String> searchSubstring(String needle, Collection<String> haystack) {
		return searchSubstring(needle, haystack.stream());
	}
	
	
	public static Optional<String> searchTwoSubstrings(String needleOne, String needleTwo, Collection<String> haystack) {
		return searchTwoSubstrings(needleOne, needleTwo, haystack.stream());
	}

	
	public static Optional<String> searchSubstring(String needle, Stream<String> haystack) {
		return haystack.filter(s -> s.contains(needle)).findFirst();
	}

	
	public static Optional<String> searchTwoSubstrings(String needleOne, String needleTwo, Stream<String> haystack) {
		return haystack.filter(s -> (s.contains(needleOne) && s.contains(needleTwo))).findFirst();
	}

	
	public static boolean contains(String needle, List<String> haystack) {
		return haystack.stream().anyMatch(s -> s.equals(needle));
	}
}
