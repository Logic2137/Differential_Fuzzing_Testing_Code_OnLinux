

import java.util.*;
import java.io.*;

public class SysPropTest 
{
		
	static final byte[] B = { (byte)0x54, (byte)0x65, (byte)0x73, (byte)0x74,  (byte)0x56, (byte)0x61,(byte) 187, (byte)0x6C, (byte)0x75, (byte)0x65, (byte) 161};	

	public static void main(String args[])
	{
		boolean isWindows = false;
		if ( args.length == 0) {
			System.out.println("test failed"); 
			return;
		}
		String argEncoding = args[0];
		
		try {
			String osEncoding = "";
			String strTestProp;
			if (System.getProperty("os.name").contains("Windows")) {
				isWindows = true;
			}

			if (argEncoding.equals("DEFAULT")) {
				osEncoding = System.getProperty("os.encoding");
			}

			
			if (osEncoding != null && osEncoding.length() != 0 && isWindows == false) {  
				strTestProp = new String(B,osEncoding);
			} else {
				if ((argEncoding.equals("UTF-8") || argEncoding.equals("ISO-8859-1"))) {
					strTestProp = new String(B,argEncoding);
				} else {
					strTestProp = new String(B);
				}
			}
			
			String strProp = System.getProperty("testkey"); 
			if (strProp == null || strTestProp.compareTo(strProp) != 0) {
				System.out.println("test failed"); 
				System.out.println("os.encoding: " + System.getProperty("os.encoding"));	
				System.out.println("file.encoding: " + System.getProperty("file.encoding"));				
				System.out.print("strProp    : ");
				for (int i=0; i < strProp.length(); i++) {
					System.out.print(Integer.toHexString(strProp.charAt(i)) + " ");
				}
				System.out.println();
				System.out.print("strTestProp: ");
				for (int i=0; i < strTestProp.length(); i++) {
					System.out.print(Integer.toHexString(strTestProp.charAt(i)) + " ");
				}
				System.out.println();				
				
			} else {
				System.out.println("test succeeded"); 
			}
		} catch(UnsupportedEncodingException e) {
			System.out.println("test failed"); 
			e.printStackTrace();
		}
	}
}
