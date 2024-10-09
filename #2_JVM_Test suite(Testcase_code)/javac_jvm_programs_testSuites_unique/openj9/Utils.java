
package com.ibm.j9.cfdump.tests.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;


@SuppressWarnings("nls")
public class Utils {

	
	private static void consumeAndIgnore(InputStream input) {
		Runnable sink = () -> {
			byte[] buffer = new byte[1024];

			try {
				while (input.read(buffer) > 0) {
					
				}

				input.close();
			} catch (IOException e) {
				
			}
		};

		Thread thread = new Thread(sink, "null-sink");

		thread.setDaemon(true);
		thread.start();
	}

	
	public static BufferedReader getBufferedErrorStreamOnly(Process process) throws IOException {
		
		process.getOutputStream().close();

		
		consumeAndIgnore(process.getInputStream());

		return new BufferedReader(new InputStreamReader(process.getErrorStream()));
	}

	
	public static Process invokeCfdumpOnClass(Class<?> klass, String arg) throws IOException {
		String sourceFile = getJarFileForClass(klass);
		String sourceFileFlag = "-z:";
		if (null == sourceFile){
			
			sourceFile = System.getProperty("java.home") + "/lib/modules";
			sourceFileFlag = "-j:";
		}
		if (!new File(sourceFile).exists()) {
			System.out.println("Could not get jar file for class: " + klass.getCanonicalName());
			return null;
		}

		String cfdump = getCfdumpPath();
		String command[] = new String[] {
			cfdump,
			arg,
			sourceFileFlag + sourceFile,
			klass.getCanonicalName()
		};

		System.out.print("Running command: ");
		for (String s : command) {
			System.out.print(s + " ");
		}
		System.out.println();

		
		File workingDir = new File(cfdump).getParentFile();
		return Runtime.getRuntime().exec(command, null, workingDir);
	}

	
	public static Process invokeCfdumpOnFile(File file, String arg) throws IOException {
		String cfdump = getCfdumpPath();
		String command[] = new String[] {
			cfdump,
			arg,
			file.getAbsolutePath()
		};

		System.out.print("Running command:");
		for (String s : command) {
			System.out.print(" " + s);
		}
		System.out.println();

		
		File workingDir = new File(cfdump).getParentFile();
		return Runtime.getRuntime().exec(command, null, workingDir);
	}

	
	public static String getCfdumpPath() {
		String[] extensions = { ".exe", "" };
		String separator = System.getProperty("path.separator");
		String[] libPathEntries = System.getProperty("java.library.path").split(separator);
		for (String path : libPathEntries) {
			for (String ext : extensions) {
				String cfdumpPath = path + "/cfdump" + ext;
				if (new File(cfdumpPath).exists()) {
					return cfdumpPath;
				}
			}
		}
		throw new RuntimeException("Error: cfdump not found on java.library.path!");
	}

	
	private static String getJarFileForClass(Class<?> klass) {
		URL url = klass.getResource(klass.getSimpleName() + ".class");
		if (url != null) {
			try {
				URLConnection connection = url.openConnection();
				if (connection instanceof JarURLConnection) {
					JarURLConnection urlConnection = (JarURLConnection) connection;
					return new File(urlConnection.getJarFileURL().getFile()).getCanonicalPath();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	
	public static boolean findLineInStream(String expectedLine, BufferedReader in) throws IOException {
		try {
			for (String line; (line = in.readLine()) != null;) {
				if (line.equals(expectedLine)) {
					return true;
				}
			}
		} finally {
			in.close();
		}
		return false;
	}

}
