

package org.reactivestreams.tck.flow.support;



public class NonFatal {
  private NonFatal() {
    
  }

  
  public static boolean isNonFatal(Throwable t) {
    if (t instanceof StackOverflowError) {
      
      return true;
    } else if (t instanceof VirtualMachineError ||
        t instanceof ThreadDeath ||
        t instanceof InterruptedException ||
        t instanceof LinkageError) {
      
      return false;
    } else {
      return true;
    }
  }
}
