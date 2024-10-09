

class Target {
    static {
        try {
            System.loadLibrary("someLibrary");
            throw new RuntimeException("someLibrary was loaded");
        } catch (UnsatisfiedLinkError e) {
            
        }
    }
}

