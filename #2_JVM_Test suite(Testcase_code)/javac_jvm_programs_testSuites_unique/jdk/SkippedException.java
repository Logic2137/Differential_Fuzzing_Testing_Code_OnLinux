

package jtreg;


public final class SkippedException extends RuntimeException {
    private static final long serialVersionUID = 1347132660681446077L;

    public SkippedException(String s, Throwable t) {
        super(s, t);
    }

    public SkippedException(String s) {
        super(s);
    }
}
