import java.util.prefs.*;

public class AddNodeChangeListener {

    private static final int SLEEP_ITRS = 10;

    private static boolean failed = false;

    private static Preferences userRoot, N2;

    private static NodeChangeListenerAdd ncla;

    public static void main(String[] args) throws BackingStoreException, InterruptedException {
        userRoot = Preferences.userRoot();
        ncla = new NodeChangeListenerAdd();
        userRoot.addNodeChangeListener(ncla);
        addNode();
        addNode();
        removeNode();
        if (failed) {
            throw new RuntimeException("Failed");
        }
    }

    private static void addNode() throws BackingStoreException, InterruptedException {
        N2 = userRoot.node("N2");
        userRoot.flush();
        int passItr = -1;
        for (int i = 0; i < SLEEP_ITRS; i++) {
            System.out.print("addNode sleep iteration " + i + "...");
            Thread.sleep(3000);
            System.out.println("done.");
            if (ncla.getAddNumber() == 1) {
                passItr = i;
                break;
            }
        }
        checkPassItr(passItr, "addNode()");
    }

    private static void removeNode() throws BackingStoreException, InterruptedException {
        N2.removeNode();
        userRoot.flush();
        int passItr = -1;
        for (int i = 0; i < SLEEP_ITRS; i++) {
            System.out.print("removeNode sleep iteration " + i + "...");
            Thread.sleep(3000);
            System.out.println("done.");
            if (ncla.getAddNumber() == 0) {
                passItr = i;
                break;
            }
        }
        checkPassItr(passItr, "removeNode()");
    }

    private static void checkPassItr(int itr, String methodName) {
        if (itr == 0) {
            System.out.println(methodName + " test passed");
        } else {
            failed = true;
            if (itr == -1) {
                throw new RuntimeException("Failed in " + methodName + " - change listener never notified");
            } else {
                throw new RuntimeException("Failed in " + methodName + " - listener notified on iteration " + itr);
            }
        }
    }

    private static class NodeChangeListenerAdd implements NodeChangeListener {

        private int totalNode = 0;

        @Override
        public void childAdded(NodeChangeEvent evt) {
            totalNode++;
        }

        @Override
        public void childRemoved(NodeChangeEvent evt) {
            totalNode--;
        }

        public int getAddNumber() {
            return totalNode;
        }
    }
}
