
package Utilities;


public class StringManipulator {

	public String getStringElement(int elementIndex, String string){
		
		char seperator = ',';
		int currentElement = -1;
		int startIndex = 0;
		int endIndex = 0;
		int currentIndex = 0;
		String element = "";
		
		while(currentElement != elementIndex){
			startIndex = currentIndex;
			endIndex = string.indexOf(seperator, currentIndex);
			if(endIndex == -1){
				element = string.substring(currentIndex);
			} else {
				currentIndex = endIndex + 1;
				element = string.substring(startIndex, endIndex);
			}
			currentElement += 1;
		}				
		return element;
	}
	
	public static String extractDummyNameSuffix(String string)
	{
		char seperator = '/';
		int startIndex = 0;
		String suffix = "";
		
		int lastIndex = string.lastIndexOf(seperator);
		for(int currentIndex = 0; currentIndex < lastIndex; currentIndex = string.indexOf(seperator, startIndex)){
			startIndex = currentIndex + 1;
		}
		suffix = string.substring(startIndex, lastIndex);
			
		return suffix.length() == 0 ? null : suffix;
	}
	
	public static String extractJarDummyNameSuffix(String string)
	{
		char seperator = '/';
		char pling = '!';
		int startIndex = 0;
		String suffix = "";
		
		int plingIndex = string.lastIndexOf(pling);
		String tempString = string.substring(startIndex, plingIndex);
		int lastIndex = tempString.lastIndexOf(seperator);
		for(int currentIndex = 0; currentIndex < lastIndex; currentIndex = tempString.indexOf(seperator, startIndex)){
			startIndex = currentIndex + 1;
		}
		suffix = tempString.substring(startIndex, lastIndex);
			
		return suffix.length() == 0 ? null : suffix;
	}
	
}
