

package gc.arguments;

import java.util.concurrent.Callable;


public final class AllocationHelper {

    private final int arrayLength;
    private final int maxIterations;
    private final int chunkSize;

    
    private static Object garbageStorage;
    private byte garbage[][];
    private final Callable<?> verifierInstance;

    
    public AllocationHelper(int maxIterations, int arrayLength, int chunkSize, Callable<?> verifier) {
        if ((arrayLength <= 0) || (maxIterations <= 0) || (chunkSize <= 0)) {
            throw new IllegalArgumentException("maxIterations, arrayLength and chunkSize should be greater then 0.");
        }
        this.arrayLength = arrayLength;
        this.maxIterations = maxIterations;
        this.chunkSize = chunkSize;
        verifierInstance = verifier;
        garbage = new byte[this.arrayLength][];
        garbageStorage = garbage;
    }

    private void allocateMemoryOneIteration() {
        for (int j = 0; j < arrayLength; j++) {
            garbage[j] = new byte[chunkSize];
        }
    }

    
    public void allocateMemoryAndVerify() throws Exception {
        for (int i = 0; i < maxIterations; i++) {
            allocateMemoryOneIteration();
            if (verifierInstance != null) {
                verifierInstance.call();
            }
        }
    }

    
    public void allocateMemoryAndVerifyNoOOME() throws Exception {
        try {
            allocateMemoryAndVerify();
        } catch (OutOfMemoryError e) {
            
        }
    }

    
    public void release() {
        if (garbage != null) {
            garbage = null;
            garbageStorage = null;
        }
    }
}
