

public final class ExternalAbuseOfVbc {

    final Integer val = Integer.valueOf(42);
    final String ref = "String";

    void abuseVbc() {
        synchronized(ref) {      
            synchronized (val) { 
            }
        }
    }
}

