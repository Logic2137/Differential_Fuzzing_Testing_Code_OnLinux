
package j9vm.runner;
import java.util.*;
import java.util.zip.*;
import java.io.*;

public class AllTestsInJar implements java.util.Enumeration {
	boolean entryReady;
	String testName;
	ZipFile zipFile;
	Enumeration zipFileEntries;

public boolean isValidTestName(String testName)  {
	if (!testName.startsWith("j9vm.test."))  {
		return false;
	}
	if (!testName.endsWith("Test"))  {
		return false;
	}
	return true;
}

public boolean hasMoreElements(){
	if (entryReady)  return true;
	while (!entryReady)  {
		if (!zipFileEntries.hasMoreElements())  {
			return false;
		}
		testName = ((ZipEntry)zipFileEntries.nextElement()).getName();
		if (testName.endsWith(".class"))  {
			testName = testName.substring(0, testName.length() - ".class".length());
			testName = testName.replace('/','.');
			entryReady = isValidTestName(testName);
		}
	}
	return true;
}
public Object nextElement(){
	if (!hasMoreElements())  throw new NoSuchElementException();
	entryReady = false;
	return testName;
}

public AllTestsInJar(String jarFileName) throws IOException {
	entryReady = false;
	testName = null;
	zipFile = new ZipFile(jarFileName);
	zipFileEntries = zipFile.entries();
}

public void finalize()  {
	try  {
		zipFile.close();
	} catch (Exception e)  {
	}
}

}
