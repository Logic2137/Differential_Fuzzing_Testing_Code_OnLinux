

package org.openj9.test.util;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileUtilities {

	
	public static void deleteRecursive(File root) throws IOException {
		deleteRecursive(root, true);
	}
	
	
	public static void deleteRecursive(File root, boolean failOnError) throws IOException {
		final String rootPath = root.getAbsolutePath();
		if (Objects.nonNull(rootPath) && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (null != children) {
					for (File c : children) {
						deleteRecursive(c, failOnError);
					}
				} else if (failOnError) {
					throw new IOException("Error listing files in  "  
							+ rootPath);
				}
			}
			if (!root.delete() && failOnError) {
				throw new IOException("Error deleting "  
						+ rootPath);			
			}
		}
	}

}
