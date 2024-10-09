



import java.util.*;

public class Test8004741 extends Thread {

  static int passed = 0;

  
  static int[][] test(int a, int b) throws Exception {
    int[][] ar;
    try {
      ar = new int[a][b];
    } catch (ThreadDeath e) {
      System.out.println("test got ThreadDeath");
      passed++;
      throw(e);
    }
    return ar;
  }

  
  Object progressLock = new Object();
  private static final int NOT_STARTED = 0;
  private static final int RUNNING = 1;
  private static final int STOPPING = 2;

  int progressState = NOT_STARTED;

  void toState(int state) {
    synchronized (progressLock) {
      progressState = state;
      progressLock.notify();
    }
  }

  void waitFor(int state) {
    synchronized (progressLock) {
      while (progressState < state) {
        try {
          progressLock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
          System.out.println("unexpected InterruptedException");
          fail();
        }
      }
      if (progressState > state) {
        System.out.println("unexpected test state change, expected " +
                            state + " but saw " + progressState);
        fail();
      }
    }
  }

  
  public void run() {
    try {
      
      
      System.out.println("thread running");
      toState(RUNNING);
      while (true) {
        
        test(2, 100);
      }
    } catch (ThreadDeath e) {
      
    } catch (Throwable e) {
      e.printStackTrace();
      System.out.println("unexpected Throwable " + e);
      fail();
    }
    toState(STOPPING);
  }

  
  public static void threadTest() throws InterruptedException {
    Test8004741 t = new Test8004741();
    t.start();
    t.waitFor(RUNNING);
    Thread.sleep(100);
    System.out.println("stopping thread");
    t.stop();
    t.waitFor(STOPPING);
    t.join();
  }

  public static void main(String[] args) throws Exception {
    
    
    for (int n = 0; n < 11000; n++) {
      test(2, 100);
    }

    
    Thread.sleep(500);
    passed = 0;

    try {
      test(-1, 100);
      System.out.println("Missing NegativeArraySizeException #1");
      fail();
    } catch ( java.lang.NegativeArraySizeException e ) {
      System.out.println("Saw expected NegativeArraySizeException #1");
    }

    try {
      test(100, -1);
      fail();
      System.out.println("Missing NegativeArraySizeException #2");
      fail();
    } catch ( java.lang.NegativeArraySizeException e ) {
      System.out.println("Saw expected NegativeArraySizeException #2");
    }

    
    int N = 12;
    for (int n = 0; n < N; n++) {
      threadTest();
    }

    if (passed > N/2) {
      System.out.println("Saw " + passed + " out of " + N + " possible ThreadDeath hits");
      System.out.println("PASSED");
    } else {
      System.out.println("Too few ThreadDeath hits; expected at least " + N/2 +
                         " but saw only " + passed);
      fail();
    }
  }

  static void fail() {
    System.out.println("FAILED");
    System.exit(97);
  }
};
