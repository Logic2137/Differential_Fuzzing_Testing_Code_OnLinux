

 

import java.util.prefs.*;

 public class AddNodeChangeListener {

     private static boolean failed = false;
     private static Preferences userRoot, N2;
     private static NodeChangeListenerAdd ncla;

     public static void main(String[] args)
         throws BackingStoreException, InterruptedException
     {
        userRoot = Preferences.userRoot();
        ncla = new NodeChangeListenerAdd();
        userRoot.addNodeChangeListener(ncla);
        
        addNode();
        
        addNode();
        
        removeNode();

        if (failed)
            throw new RuntimeException("Failed");
    }

    private static void addNode()
        throws BackingStoreException, InterruptedException
    {
        N2 = userRoot.node("N2");
        userRoot.flush();
        Thread.sleep(3000);
        if (ncla.getAddNumber() != 1)
            failed = true;
    }

    private static void removeNode()
        throws BackingStoreException, InterruptedException
    {
        N2.removeNode();
        userRoot.flush();
        Thread.sleep(3000);
        if (ncla.getAddNumber() != 0)
            failed = true;
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

        public int getAddNumber(){
            return totalNode;
        }
    }
 }
