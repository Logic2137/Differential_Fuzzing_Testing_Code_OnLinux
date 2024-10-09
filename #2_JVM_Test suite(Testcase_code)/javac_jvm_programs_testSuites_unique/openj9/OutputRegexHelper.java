

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OutputRegexHelper {

	public static boolean ContainsMatches(String data, String regex, boolean matchCase, boolean showRegexMatch, String type) {
		try {
			Pattern p = null;
			if (matchCase) {
				p = Pattern.compile(regex);
			} else {
				p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			}
			Matcher m = p.matcher(data);
			boolean retval = m.find();
			if(	retval && showRegexMatch) {
				int start = m.start();
				int end = m.end();				
				System.out.println("\tMatch ("+type+"): "+data.substring(start, end));
			}
			return retval;
		} catch (Exception e) {
			System.out.println("Exception " + e.getClass().toString() + " message " + e.getMessage());
			System.out.println("Regex:" + regex);
			e.printStackTrace();
			return false;
		}
		
	}
	
}
