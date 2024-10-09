

package jdk.testlibrary;


public abstract class XRun implements Runnable {

    
    public final void run() {
        try {
            xrun();
        } catch (Error e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    
    protected abstract void xrun() throws Throwable;
}
