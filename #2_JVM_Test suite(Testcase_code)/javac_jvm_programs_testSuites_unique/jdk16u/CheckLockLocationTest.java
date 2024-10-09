


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.UserPrincipal;
import java.util.UUID;
import java.util.logging.FileHandler;
public class CheckLockLocationTest {

    private static final String NON_WRITABLE_DIR = "non-writable-dir";
    private static final String NOT_A_DIR = "not-a-dir";
    private static final String WRITABLE_DIR = "writable-dir";
    private static final String NON_EXISTENT_DIR = "non-existent-dir";
    private static boolean runNonWritableDirTest;

    public static void main(String... args) throws IOException {
        
        
        
        
        
        
        
        
        File writableDir = setup();
        
        
        
        
        
        runTests(writableDir);
    }

    
    private static void runTests(File writableDir) throws SecurityException,
            RuntimeException, IOException {
        
        try {
            new FileHandler("%t/" + WRITABLE_DIR + "/log.log");
        } catch (IOException ex) {
            throw new RuntimeException("Test failed: should have been able"
                    + " to create FileHandler for " + "%t/" + WRITABLE_DIR
                    + "/log.log in writable directory"
                    + (!writableDir.canRead() 
                        ? ": directory not readable.\n\tPlease check your "
                         + "environment and machine configuration."
                        : "."), ex);
        } finally {
            
            
            delete(writableDir);
        }

        
        if (runNonWritableDirTest) {
            try {
                new FileHandler("%t/" + NON_WRITABLE_DIR + "/log.log");
                throw new RuntimeException("Test failed: should not have been able"
                        + " to create FileHandler for " + "%t/" + NON_WRITABLE_DIR
                        + "/log.log in non-writable directory.");
            } catch (AccessDeniedException ex) {
                
            } catch (IOException ex) {
                throw new RuntimeException(
                        "Test failed: Expected exception was not an "
                                + "AccessDeniedException", ex);
            }
        }

        
        try {
            new FileHandler("%t/" + NOT_A_DIR + "/log.log");
            throw new RuntimeException("Test failed: should not have been able"
                    + " to create FileHandler for " + "%t/" + NOT_A_DIR
                    + "/log.log in non-directory.");
        } catch (FileSystemException ex) {
            
        } catch (IOException ex) {
            throw new RuntimeException("Test failed: exception thrown was not a "
                    + "FileSystemException", ex);
        }

        
        try {
            new FileHandler("%t/" + NON_EXISTENT_DIR + "/log.log");
            throw new RuntimeException("Test failed: should not have been able"
                    + " to create FileHandler for " + "%t/" + NON_EXISTENT_DIR
                    + "/log.log in a non-existent directory.");
        } catch (NoSuchFileException ex) {
            
        } catch (IOException ex) {
            throw new RuntimeException("Test failed: Expected exception "
                    + "was not a NoSuchFileException", ex);
        }
    }

    
    private static File setup() throws RuntimeException {
        
        
        String tmpDir = System.getProperty("java.io.tmpdir"); 
        if (tmpDir == null) {
            tmpDir = System.getProperty("user.home");
        }
        File tmpOrHomeDir = new File(tmpDir);
        
        File writableDir = new File(tmpOrHomeDir, WRITABLE_DIR);
        if (!createFile(writableDir, true)) {
            throw new RuntimeException("Test setup failed: unable to create"
                    + " writable working directory "
                    + writableDir.getAbsolutePath() );
        }

        if (!writableDir.canRead()) {
            throw new RuntimeException("Test setup failed: can't read "
                    + " writable working directory "
                    + writableDir.getAbsolutePath() );
        }

        
        

        
        File dummyFile = new File(writableDir, UUID.randomUUID().toString() + ".txt" );
        try {
            if (!dummyFile.createNewFile()) {
                throw new RuntimeException("Test setup failed: can't create "
                        + " dummy file in writable working directory "
                        + dummyFile.getAbsolutePath() );
            }
            try (OutputStream os = new FileOutputStream(dummyFile)) {
                os.write('A');
            } finally {
                dummyFile.delete();
            }
            if (dummyFile.canRead()) {
                throw new RuntimeException("Test setup failed: can't delete "
                        + " dummy file in writable working directory "
                        + dummyFile.getAbsolutePath() );
            }
            System.out.println("Successfully created and deleted dummy file: " +
                dummyFile.getAbsolutePath());
        } catch(IOException x) {
            throw new RuntimeException("Test setup failed: can't write "
                        + " or delete dummy file in writable working directory "
                        + dummyFile.getAbsolutePath(), x);
        }

        
        
        File notAdir = new File(tmpOrHomeDir, NOT_A_DIR);
        if (!createFile(notAdir, false)) {
            throw new RuntimeException("Test setup failed: unable to a plain"
                    + " working file " + notAdir.getAbsolutePath() );
        }
        notAdir.deleteOnExit();

        
        File nonWritableDir = new File(tmpOrHomeDir, NON_WRITABLE_DIR);
        if (!createFile(nonWritableDir, true)) {
            throw new RuntimeException("Test setup failed: unable to create"
                    + " a non-"
                    + "writable working directory "
                    + nonWritableDir.getAbsolutePath() );
        }
        nonWritableDir.deleteOnExit();

        
        Path path = nonWritableDir.toPath();
        final boolean nonWritable = nonWritableDir.setWritable(false);
        final boolean isWritable = Files.isWritable(path);
        if (nonWritable && !isWritable) {
            runNonWritableDirTest = true;
            System.out.println("Created non writable dir for "
                    + getOwner(path) + " at: " + path.toString());
        } else {
            runNonWritableDirTest = false;
            System.out.println( "Test Setup WARNING: unable to make"
                    + " working directory " + nonWritableDir.getAbsolutePath()
                    + "\n\t non-writable for " + getOwner(path)
                    +  " on platform " + System.getProperty("os.name"));
        }

        
        File nonExistentDir = new File(tmpOrHomeDir, NON_EXISTENT_DIR);
        if (nonExistentDir.exists()) {
            nonExistentDir.delete();
        }
        System.out.println("Setup completed - writableDir is: " + writableDir.getPath());
        return writableDir;
    }

    private static String getOwner(Path path) {
        UserPrincipal user = null;
        try {
            user = Files.getOwner(path);
        } catch (Exception x) {
            System.err.println("Failed to get owner of: " + path);
            System.err.println("\terror is: " + x);
        }
        return user == null ? "???" : user.getName();
    }

    
    private static boolean createFile(File newFile, boolean makeDirectory) {
        if (newFile.exists()) {
            return true;
        }
        if (makeDirectory) {
            return newFile.mkdir();
        } else {
            try {
                return newFile.createNewFile();
            } catch (IOException ioex) {
                ioex.printStackTrace();
                return false;
            }
        }
    }

    
    private static void delete(File f) {
        if (f != null && f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            System.err.println(
                    "WARNING: unable to delete/cleanup writable test directory: "
                    + f );
        }
}
