

package p.one;

public interface I {
    void run();

    default int m() { return 1; }
}
