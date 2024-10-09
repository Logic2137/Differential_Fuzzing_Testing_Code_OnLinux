

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;



public class GetConsoleCharset {
	public static void main(String[] args) throws Exception {
		OutputStream stream;
		if (args.length < 2) {
			System.out.println("Provide two arguments, the first is either 'out' or 'err', and the second is the expected Charset name.");
			System.exit(1);
		}
		if ("out".equals(args[0])) {
			stream = System.out;
		} else {
			stream = System.err;
		}
		Field charOutField = PrintStream.class.getDeclaredField("charOut");
		charOutField.setAccessible(true);
		String encoding = ((OutputStreamWriter)charOutField.get(stream)).getEncoding();
		System.out.println("System." + args[0] + " encoding: " + encoding);
		if (args.length > 2) {
			FileOutputStream out = new FileOutputStream("GetConsoleCharset-result.txt");
			out.write(encoding.getBytes("ISO-8859-1"));
			out.close();
		}
		if (encoding.equals(args[1])) {
			System.out.println("SUCCESS");
			System.exit(0);
		}
		System.out.println("FAILED");
		System.exit(1);
	}
}
