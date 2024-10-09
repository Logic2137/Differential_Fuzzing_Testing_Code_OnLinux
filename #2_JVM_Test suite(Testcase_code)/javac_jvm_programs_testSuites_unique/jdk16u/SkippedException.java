

package jtreg;


public final class SkippedException extends RuntimeException {
    public SkippedException(String s, Throwable t) {
        super(s, t);
    }

    public SkippedException(String s) {
        super(s);
    }
}
