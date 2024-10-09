


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class JarIndexMergeForClassLoaderTest {
    static final String slash = File.separator;
    static final String testClassesDir = System.getProperty("test.classes", ".");
    static final String jar;
    static final boolean debug = true;
    static final File tmpFolder = new File(testClassesDir);

    static {
        jar = System.getProperty("java.home") + slash + "bin" + slash + "jar";
    }

    public static void main(String[] args) throws Exception {
        
        File jar1 = buildJar1();
        File jar2 = buildJar2();
        File jar3 = buildJar3();

        
        createIndex(jar2.getName(), jar3.getName());
        createIndex(jar1.getName(), jar2.getName());

        
        URL url = jar1.toURI().toURL();

        URLClassLoader classLoader = new URLClassLoader(new URL[] { url });

        assertResource(classLoader, "com/jar1/resource.file", "jar1");
        assertResource(classLoader, "com/test/resource1.file", "resource1");
        assertResource(classLoader, "com/jar2/resource.file", "jar2");
        assertResource(classLoader, "com/test/resource2.file", "resource2");
        assertResource(classLoader, "com/test/resource3.file", "resource3");

        
        
        assertResource(classLoader, "com/missing/jar3/resource.file", "jar3");
        
        
        assertResource(classLoader, "com/missing/nofile", null);
    }

    private static File buildJar3() throws FileNotFoundException, IOException {
        JarBuilder jar3Builder = new JarBuilder(tmpFolder, "jar3.jar");
        jar3Builder.addResourceFile("com/test/resource3.file", "resource3");
        jar3Builder.addResourceFile("com/missing/jar3/resource.file", "jar3");
        return jar3Builder.build();
    }

    private static File buildJar2() throws FileNotFoundException, IOException {
        JarBuilder jar2Builder = new JarBuilder(tmpFolder, "jar2.jar");
        jar2Builder.addResourceFile("com/jar2/resource.file", "jar2");
        jar2Builder.addResourceFile("com/test/resource2.file", "resource2");
        return jar2Builder.build();
    }

    private static File buildJar1() throws FileNotFoundException, IOException {
        JarBuilder jar1Builder = new JarBuilder(tmpFolder, "jar1.jar");
        jar1Builder.addResourceFile("com/jar1/resource.file", "jar1");
        jar1Builder.addResourceFile("com/test/resource1.file", "resource1");
        return jar1Builder.build();
    }

    
    static void createIndex(String parentJar, String childJar) {
        
        
        debug("Running jar to create the index for: " + parentJar + " and "
                + childJar);
        ProcessBuilder pb = new ProcessBuilder(jar, "-i", parentJar, childJar);

        pb.directory(tmpFolder);
        
        try {
            Process p = pb.start();
            if (p.waitFor() != 0)
                throw new RuntimeException("jar indexing failed");

            if (debug && p != null) {
                debugStream(p.getInputStream());
                debugStream(p.getErrorStream());
            }
        } catch (InterruptedException | IOException x) {
            throw new RuntimeException(x);
        }
    }

    private static void debugStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                debug(line);
            }
        }
    }

    private static void assertResource(URLClassLoader classLoader, String file,
            String expectedContent) throws IOException {
        InputStream fileStream = classLoader.getResourceAsStream(file);

        if (fileStream == null && expectedContent == null) {
            return;
        }
        if (fileStream == null && expectedContent != null) {
            throw new RuntimeException(
                    buildMessage(file, expectedContent, null));
        }
        try {
            String actualContent = readAsString(fileStream);

            if (fileStream != null && expectedContent == null) {
                throw new RuntimeException(buildMessage(file, null,
                        actualContent));
            }
            if (!expectedContent.equals(actualContent)) {
                throw new RuntimeException(buildMessage(file, expectedContent,
                        actualContent));
            }
        } finally {
            fileStream.close();
        }
    }

    private static String buildMessage(String file, String expectedContent,
            String actualContent) {
        return "Expected: " + expectedContent + " for: " + file + " was: "
                + actualContent;
    }

    private static String readAsString(InputStream fileStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int count, len = 0;
        while ((count = fileStream.read(buffer, len, buffer.length-len)) != -1)
                len += count;
        return new String(buffer, 0, len, "ASCII");
    }

    static void debug(Object message) {
        if (debug)
            System.out.println(message);
    }

    
    public static class JarBuilder {
        private JarOutputStream os;
        private File jarFile;

        public JarBuilder(File tmpFolder, String jarName)
            throws FileNotFoundException, IOException
        {
            this.jarFile = new File(tmpFolder, jarName);
            this.os = new JarOutputStream(new FileOutputStream(jarFile));
        }

        public void addResourceFile(String pathFromRoot, String content)
            throws IOException
        {
            JarEntry entry = new JarEntry(pathFromRoot);
            os.putNextEntry(entry);
            os.write(content.getBytes("ASCII"));
            os.closeEntry();
        }

        public File build() throws IOException {
            os.close();
            return jarFile;
        }
    }
}

