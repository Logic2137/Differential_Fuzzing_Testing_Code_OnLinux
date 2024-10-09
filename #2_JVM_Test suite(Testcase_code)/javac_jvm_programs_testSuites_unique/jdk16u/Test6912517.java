



package compiler.c2;


public class Test6912517 implements Runnable
{
    private final Thread myThread;
    private Thread       myInitialThread;
    private boolean      myShouldCheckThreads;

    
    public Test6912517(int id)
    {
        myThread = new Thread(this);
        myThread.setName("Runner: " + id);
        myThread.start();
        myShouldCheckThreads = false;
    }

    
    public void setShouldCheckThreads(boolean shouldCheckThreads)
    {
        myShouldCheckThreads = shouldCheckThreads;
    }

    
    public static void main(String[] args) throws InterruptedException
    {
        
        for (int id = 0; id < 20; id++) {
            System.out.println("Starting thread: " + id);
            Test6912517 bug = new Test6912517(id);
            bug.setShouldCheckThreads(true);
            Thread.sleep(2500);
        }
    }

    
    public void run()
    {
        long runNumber = 0;
        while (true) {
            
            
            if (runNumber > 15000) {
                try {
                    Thread.sleep(5);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runNumber++;
            ensureProperCallingThread();
        }
    }

    private void ensureProperCallingThread()
    {
        
        
        if (myShouldCheckThreads) {
            if (myInitialThread == null) {
                myInitialThread = Thread.currentThread();
            }
            else if (myInitialThread != Thread.currentThread()) {
                System.out.println("Not working: " + myInitialThread.getName());
            }
        }
    }
}
