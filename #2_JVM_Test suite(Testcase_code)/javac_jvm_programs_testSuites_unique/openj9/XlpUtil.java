

package j9vm.test.xlphelper;

public class XlpUtil {
	
	public static final String XLP_PAGE_TYPE_NOT_USED = "not used";
	public static final String XLP_PAGE_TYPE_PAGEABLE = "pageable";
	public static final String XLP_PAGE_TYPE_NONPAGEABLE = "nonpageable";
	
	
	public static long pageSizeStringToLong(String pageSizeString) {
		long pageSizeInBytes = 0;
		long pageSizeQualifier = 0;
		boolean invalidQualifier = false;
		String qualifier = pageSizeString.substring(pageSizeString.length()-1);		

		if (qualifier.matches("[a-zA-Z]")) {
			switch(qualifier.charAt(0)) {
			case 'k':
			case 'K':
				pageSizeQualifier = 10;
				break;
			case 'm':
			case 'M':
				pageSizeQualifier = 20;
				break;
			case 'g':
			case 'G':
				pageSizeQualifier = 30;
				break;
			default:
				System.out.println("ERROR: Unrecognized qualifier found in page size string");
				invalidQualifier = true;
				break;
			}
		}
		if (invalidQualifier) {
			pageSizeInBytes = 0;			
		} else {
			long pageSizeValue = 0;
			if (pageSizeQualifier != 0) {
				
				pageSizeValue = Long.parseLong(pageSizeString.substring(0, pageSizeString.length()-1));
			} else {
				
				pageSizeValue = Long.parseLong(pageSizeString.substring(0, pageSizeString.length()));
			}
			pageSizeInBytes = pageSizeValue << pageSizeQualifier;
		}
		return pageSizeInBytes;
	}

}
