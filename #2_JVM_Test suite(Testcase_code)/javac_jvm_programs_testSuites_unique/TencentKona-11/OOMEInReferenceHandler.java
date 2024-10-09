



import java.lang.ref.*;

public class OOMEInReferenceHandler {
     static Object[] fillHeap() {
         Object[] first = null, last = null;
         int size = 1 << 20;
         while (size > 0) {
             try {
                 Object[] array = new Object[size];
                 if (first == null) {
                     first = array;
                 } else {
                     last[0] = array;
                 }
                 last = array;
             } catch (OutOfMemoryError oome) {
                 size = size >>> 1;
             }
         }
         return first;
     }

     public static void main(String[] args) throws Exception {
         
         
         InterruptedException ie = new InterruptedException("dummy");

         ThreadGroup tg = Thread.currentThread().getThreadGroup();
         for (
             ThreadGroup tgn = tg;
             tgn != null;
             tg = tgn, tgn = tg.getParent()
             )
             ;

         Thread[] threads = new Thread[tg.activeCount()];
         Thread referenceHandlerThread = null;
         int n = tg.enumerate(threads);
         for (int i = 0; i < n; i++) {
             if ("Reference Handler".equals(threads[i].getName())) {
                 referenceHandlerThread = threads[i];
             }
         }

         if (referenceHandlerThread == null) {
             throw new IllegalStateException("Couldn't find Reference Handler thread.");
         }

         ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
         Object referent = new Object();
         WeakReference<Object> weakRef = new WeakReference<>(referent, refQueue);

         Object waste = fillHeap();

         referenceHandlerThread.interrupt();

         
         Thread.sleep(500L);

         
         waste = null;
         referent = null;

         
         for (int i = 0; i < 20; i++) {
             if (refQueue.poll() != null) {
                 
                 return;
             }
             System.gc();
             Thread.sleep(500L); 
             if (!referenceHandlerThread.isAlive()) {
                 
                 throw new Exception("Reference Handler thread died.");
             }
         }

         
         throw new IllegalStateException("Reference Handler thread stuck. weakRef.get(): " + weakRef.get());
     }
}
