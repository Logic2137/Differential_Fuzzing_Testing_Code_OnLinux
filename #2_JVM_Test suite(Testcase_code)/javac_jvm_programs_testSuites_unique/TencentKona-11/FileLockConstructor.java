

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;


public class FileLockConstructor {
    public static void main(String[] args) throws IOException {
        FileLock fileLock = null;
        int failures = 0;

        
        boolean exceptionThrown = false;
        try {
            fileLock = new FileLockSub((FileChannel)null, 0, 0, false);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            System.err.println("FileLock constructor did not throw NPE for null FileChannel");
            failures++;
        }

        
        exceptionThrown = false;
        try {
            fileLock = new FileLockSub((AsynchronousFileChannel)null, 0, 0, true);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        if (!exceptionThrown) {
            System.err.println("FileLock constructor did not throw NPE for null AsynchronousFileChannel");
            failures++;
        }

        
        File tmpFile = File.createTempFile("FileLock", "tmp");
        tmpFile.deleteOnExit();

        
        long[][] posAndSize = new long[][] {
            {0, 42},            
            {-1, 42},           
            {0, -1},            
            {Long.MAX_VALUE, 1} 
        };

        
        try (FileChannel syncChannel = FileChannel.open(tmpFile.toPath(),
                StandardOpenOption.READ, StandardOpenOption.WRITE)) {

            for (int i = 0; i < posAndSize.length; i++) {
                boolean preconditionsHold = i == 0;
                exceptionThrown = false;
                try {
                    fileLock = new FileLockSub(syncChannel, posAndSize[i][0],
                            posAndSize[i][1], true);
                } catch (IllegalArgumentException iae) {
                    exceptionThrown = true;
                } catch (Exception e) {
                    System.err.println("Unexpected exception \"" + e + "\" caught"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for FileChannel variant");
                    failures++;
                    continue;
                }
                if (preconditionsHold && exceptionThrown) {
                    System.err.println("FileLock constructor incorrectly threw IAE"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for FileChannel variant");
                    failures++;
                } else if (!preconditionsHold && !exceptionThrown) {
                    System.err.println("FileLock constructor did not throw IAE"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for FileChannel variant");
                    failures++;
                }
            }
        }

        
        try (AsynchronousFileChannel asyncChannel
                = AsynchronousFileChannel.open(tmpFile.toPath(),
                        StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            for (int i = 0; i < posAndSize.length; i++) {
                boolean preconditionsHold = i == 0;
                exceptionThrown = false;
                try {
                    fileLock = new FileLockSub(asyncChannel, posAndSize[i][0],
                            posAndSize[i][1], true);
                } catch (IllegalArgumentException iae) {
                    exceptionThrown = true;
                } catch (Exception e) {
                    System.err.println("Unexpected exception \"" + e + "\" caught"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for AsynchronousFileChannel variant");
                    failures++;
                    continue;
                }
                if (preconditionsHold && exceptionThrown) {
                    System.err.println("FileLock constructor incorrectly threw IAE"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for AsynchronousFileChannel variant");
                    failures++;
                } else if (!preconditionsHold && !exceptionThrown) {
                    System.err.println("FileLock constructor did not throw IAE"
                            + " for position " + posAndSize[i][0] + " and size "
                            + posAndSize[i][1] + " for AsynchronousFileChannel variant");
                    failures++;
                }
            }
        }

        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures +
                                       " failures while testing FileLock.");
        }
    }
}

class FileLockSub extends FileLock {
    FileLockSub(FileChannel channel, long position, long size, boolean shared) {
        super(channel, position, size, shared);
    }

    FileLockSub(AsynchronousFileChannel channel, long position, long size,
                boolean shared) {
        super(channel, position, size, shared);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void release() throws IOException {
        
    }
}
