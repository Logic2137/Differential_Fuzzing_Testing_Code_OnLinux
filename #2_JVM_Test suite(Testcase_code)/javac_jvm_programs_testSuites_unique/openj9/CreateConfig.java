

package tests.sharedclasses;

import java.io.FileWriter;


public class CreateConfig {

	public static void main(String[] args) throws Exception {



		
		
















		
		String javaForTesting = System.getProperty("testjava");
		String cacheDir = System.getProperty("cachedir");
		String java5 = System.getProperty("refjava");
		
		
		javaForTesting = javaForTesting.replace('\\','/');
		cacheDir = cacheDir.replace('\\','/');
		java5 = java5.replace('\\','/');
		
		FileWriter writer = new FileWriter("config.properties");
		writer.write("# Java to be tested\n");
		writer.write("java_exe="+javaForTesting+"\n\n");
		writer.write("# Cache directory\n");
		writer.write("cacheDir="+cacheDir+"\n\n");
		writer.write("# Old jdk for creating old caches\n");
		writer.write("java5_exe="+java5+"\n");
		writer.flush();
		writer.close();
	}
	
}
