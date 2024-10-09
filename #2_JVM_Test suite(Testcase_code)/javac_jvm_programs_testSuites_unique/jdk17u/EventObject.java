
package nsk.share.jvmti.attach.loader;

public class EventObject {

    private String name = null;

    private byte[] hugeBuffer;

    public EventObject(String name) {
        this.name = name;
    }

    public void setBreakPointHere() {
        System.out.println(" Here Default Break point can be set");
    }

    public void createHeap(int size) {
        hugeBuffer = new byte[size];
    }

    public void unAllocateMem() {
        hugeBuffer = null;
        System.gc();
    }
}
