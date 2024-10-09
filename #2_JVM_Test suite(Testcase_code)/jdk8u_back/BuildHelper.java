
package com.oracle.java.testlibrary;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class BuildHelper {

    public static boolean isCommercialBuild() throws Exception {
        String buildType = getReleaseProperty("BUILD_TYPE", "notFound");
        return buildType.equals("commercial");
    }

    public static String getReleaseProperty(String key, String defaultValue) throws Exception {
        Properties properties = getReleaseProperties();
        String value = properties.getProperty(key, defaultValue);
        return trimDoubleQuotes(value);
    }

    public static String getReleaseProperty(String key) throws Exception {
        return getReleaseProperty(key, null);
    }

    public static Properties getReleaseProperties() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader(getReleaseFile()));
        return properties;
    }

    public static File getReleaseFile() throws Exception {
        String jdkPath = getJDKRoot();
        File releaseFile = new File(jdkPath, "release");
        if (!releaseFile.canRead()) {
            throw new Exception("Release file is not readable, or it is absent: " + releaseFile.getCanonicalPath());
        }
        return releaseFile;
    }

    public static String getJDKRoot() {
        String jdkPath = System.getProperty("test.jdk");
        if (jdkPath == null) {
            throw new RuntimeException("System property 'test.jdk' not set. This property is normally set by jtreg. " + "When running test separately, set this property using '-Dtest.jdk=/path/to/jdk'.");
        }
        return jdkPath;
    }

    public static String trimDoubleQuotes(String original) {
        if (original == null) {
            return null;
        }
        String trimmed = original.replaceAll("^\"+|\"+$", "");
        return trimmed;
    }
}
