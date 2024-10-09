



import java.io.*;
import java.util.*;
import java.util.zip.*;

import javax.tools.*;
import javax.tools.JavaFileManager.Location;

import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

public class T6838467 {

    enum FileKind {
        DIR("dir"),
        ZIP("zip");
        FileKind(String path) {
            file = new File(path);
        }
        final File file;
    };

    enum CompareKind {
        SAME {
            @Override
            File other(File f) { return f; }
        },
        ABSOLUTE {
            @Override
            File other(File f) { return f.getAbsoluteFile(); }
        },
        DIFFERENT {
            @Override
            File other(File f) { return new File("not_" + f.getPath()); }
        },
        CASEEQUIV {
            @Override
            File other(File f) { return new File(f.getPath().toUpperCase()); }
        };
        abstract File other(File f);
    };

    String[] paths = { "p/A.java", "p/B.java", "p/C.java" };

    public static void main(String... args) throws Exception {
        new T6838467().run();
    }

    void run() throws Exception {
        boolean fileNameIsCaseSignificant = isFileNameCaseSignificant();
        boolean fileLookupIsCaseSignificant = isFileLookupCaseSignificant();

        String osName = System.getProperty("os.name");
        System.err.println("OS: " + osName);
        System.err.println("fileNameIsCaseSignificant:" + fileNameIsCaseSignificant);
        System.err.println("fileLookupIsCaseSignificant:" + fileLookupIsCaseSignificant);

        
        if ((osName.startsWith("windows")) && fileNameIsCaseSignificant) {
            error("fileNameIsCaseSignificant is set on " + osName + ".");
        }

        
        createTestDir(new File("dir"), paths);
        createTestDir(new File("not_dir"), paths);
        createTestZip(new File("zip"), paths);
        createTestZip(new File("not_zip"), paths);
        if (fileNameIsCaseSignificant || fileLookupIsCaseSignificant) {
            createTestDir(new File("DIR"), paths);
            createTestZip(new File("ZIP"), paths);
        }

        
        
        for (FileKind fk: FileKind.values()) {
            for (CompareKind ck: CompareKind.values()) {
                test(fk, ck);
            }
        }

        
        
        Set<String> expectClasses = new HashSet<>(Arrays.asList(
                "DirectoryFileObject",
                "JarFileObject" ));
        if (!foundClasses.equals(expectClasses)) {
            error("expected fileobject classes not found\n"
                    + "expected: " + expectClasses + "\n"
                    + "found: " + foundClasses);
        }

        if (errors > 0)
            throw new Exception(errors + " errors");
    }

    void test(FileKind fk, CompareKind ck) throws IOException {
        try (StandardJavaFileManager fm = createFileManager()) {
            File f1 = fk.file;
            Location l1 = createLocation(fm, "l1", f1);

            File f2 = ck.other(fk.file);
            Location l2 = createLocation(fm, "l2", f2);

            
            
            
            
            
            int expectEqualCount = (f1.getCanonicalFile().equals(f2.getCanonicalFile()) ? paths.length : 0);

            System.err.println("test " + (++count) + " " + fk + " " + ck + " " + f1 + " " + f2);
            test(fm, l1, l2, expectEqualCount);
        }
    }

    
    
    
    
    
    void test(JavaFileManager fm, Location l1, Location l2, int expectEqualCount) throws IOException {
        boolean foundFiles1 = false;
        boolean foundFiles2 = false;
        int foundEqualCount = 0;
        Set<JavaFileObject.Kind> kinds =  EnumSet.allOf(JavaFileObject.Kind.class);
        for (FileObject fo1: fm.list(l1, "p", kinds, false)) {
            foundFiles1 = true;
            foundClasses.add(fo1.getClass().getSimpleName());
            for (FileObject fo2: fm.list(l2, "p", kinds, false)) {
                foundFiles2 = true;
                foundClasses.add(fo2.getClass().getSimpleName());
                System.err.println("compare " + fo1 + " " + fo2);
                if (fo1.equals(fo2)) {
                    foundEqualCount++;
                    int hash1 = fo1.hashCode();
                    int hash2 = fo2.hashCode();
                    if (hash1 != hash2)
                        error("hashCode error: " + fo1 + " [" + hash1 + "] "
                                + fo2 + " [" + hash2 + "]");
                }
            }
        }
        if (!foundFiles1)
            error("no files found for location " + l1);
        if (!foundFiles2)
            error("no files found for location " + l2);
        
        if (foundEqualCount != expectEqualCount)
            error("expected matches not found: expected " + expectEqualCount + ", found " + foundEqualCount);
    }

    
    
    Location createLocation(StandardJavaFileManager fm, String name, File classpath) throws IOException {
        Location l = new Location() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean isOutputLocation() {
                return false;
            }

        };
        fm.setLocation(l, Arrays.asList(classpath));
        return l;
    }

    JavacFileManager createFileManager() {
        Context ctx = new Context();
        return new JavacFileManager(ctx, false, null);
    }

    
    void createTestDir(File dir, String[] paths) throws IOException {
        for (String p: paths) {
            File file = new File(dir, p);
            file.getParentFile().mkdirs();
            try (FileWriter out = new FileWriter(file)) {
                out.write(p);
            }
        }
    }

    
    void createTestZip(File zip, String[] paths) throws IOException {
        if (zip.getParentFile() != null)
            zip.getParentFile().mkdirs();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip))) {
            for (String p: paths) {
                ZipEntry ze = new ZipEntry(p);
                zos.putNextEntry(ze);
                byte[] bytes = p.getBytes();
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
        }
    }

    void error(String msg) {
        System.err.println("Error: " + msg);
        errors++;
    }

    boolean isFileNameCaseSignificant() {
        File lower = new File("test.txt");
        File upper = new File(lower.getPath().toUpperCase());
        return !lower.equals(upper);
    }

    boolean isFileLookupCaseSignificant() throws IOException {
        File lower = new File("test.txt");
        File upper = new File(lower.getPath().toUpperCase());
        if (upper.exists()) {
            upper.delete();
        }
        try (FileWriter out = new FileWriter(lower)) { }
        return !upper.exists();
    }

    int count;
    int errors;
    Set<String> foundClasses = new HashSet<>();
}

