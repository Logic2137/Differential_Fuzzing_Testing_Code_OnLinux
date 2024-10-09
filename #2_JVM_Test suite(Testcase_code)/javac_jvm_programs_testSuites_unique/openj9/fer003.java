
package com.ibm.jvmti.tests.forceEarlyReturn;




public class fer003 
{
    static Object runForestRun = new Object();
    static volatile boolean forestIsRunning;
    
    static volatile boolean testPassed = false;


    
    static final int VALUE_INT_DEFAULT = 100;
    static final int VALUE_INT_FER = 1000;
    
    static final String VALUE_OBJ_DEFAULT = "default";
    static final String VALUE_OBJ_FER = "fer";
       
    static final long VALUE_LONG_DEFAULT = 100;
    static final long VALUE_LONG_FER = 100020003000L;

    static final float VALUE_FLOAT_DEFAULT = 100;
    static final float VALUE_FLOAT_FER = 1000;

    static final double VALUE_DOUBLE_DEFAULT = 100;
    static final double VALUE_DOUBLE_FER = 100020003000.0;
    
    
    
    static final int FER_INT     = 104;
    static final int FER_LONG    = 105;
    static final int FER_FLOAT   = 106;
    static final int FER_DOUBLE  = 107;
    static final int FER_VOID    = 116;
    static final int FER_OBJECT  = 109;    
	
    native static boolean forceEarlyReturn(Thread t, int retType, Object testObject);
    
    
    
    
    public boolean testIntFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_INT);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_INT, null);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }

    public boolean testLongFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_LONG);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_LONG, null);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }
   
    public boolean testFloatFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_FLOAT);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_FLOAT, null);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }
    
    public boolean testDoubleFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_DOUBLE);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_DOUBLE, null);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }
    
    public boolean testObjectFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_OBJECT);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_OBJECT, VALUE_OBJ_FER);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }
    
    public boolean testVoidFER()
    {
    	boolean ret;
    	
    	FERThread t = new FERThread(FER_VOID);
    
    	forestIsRunning = false;
    	
    	synchronized (runForestRun) {
    		
    		if (!waitForThreadStart(t))
    			return false;
    	
    		ret = forceEarlyReturn(t, FER_VOID, null);
	    	
        	t.keepRunning = false;
    	}
    	
   		if (!waitForThreadStop(t))
			ret = false;
    	    	    	
    	return ret;
    }
    
    
    
    public String helpIntFER()
    {
    	return "test forced return of correct int value";
    }
    public String helpLongFER()
    {
    	return "test forced return of correct long value";
    }
    public String helpFloatFER()
    {
    	return "test forced return of correct float value";
    }
    public String helpDoubleFER()
    {
    	return "test forced return of correct double value";
    }
    public String helpObjectFER()
    {
    	return "test forced return of correct object value";
    }
    public String helpVoidFER()
    {
    	return "test forced return of a void method";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    private boolean waitForThreadStart(Thread t)
    {
    	t.start();
		while (!forestIsRunning) {
			try {
				runForestRun.wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
    }
    
    private boolean waitForThreadStop(Thread t)
    {
    	try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
    }
}





class FERThread extends Thread 
{
	public int runType = 0;
    public volatile boolean keepRunning = true;
  
    public static volatile int calc = 0;

    FERThread(int runType)
    {
    	this.runType = runType;
    }
    
    public void run()
    {    	
    	fer003.testPassed = false;
    	
    	switch (runType) {
    	
    		case fer003.FER_INT:
    			int intRet = testInt();
    			if (intRet != fer003.VALUE_INT_FER) {
    				System.out.println("Incorrectly returned: " + intRet);
    				return;
    			}    			
    			break;
    			
    		case fer003.FER_LONG:
    			long longRet = testLong();
    			if (longRet != fer003.VALUE_LONG_FER) {
    				System.out.println("Incorrectly returned: " + longRet);
    				return;
    			}
    			break;
    			
    		case fer003.FER_FLOAT:
    			float floatRet = testFloat();
    			if (floatRet != fer003.VALUE_FLOAT_FER) {
    				System.out.println("Incorrectly returned: " + floatRet);
    		    	return;
    			}
    			break;
    			
    		case fer003.FER_DOUBLE:
      			double doubleRet = testDouble();
    			if (doubleRet != fer003.VALUE_DOUBLE_FER) {
    				System.out.println("Incorrectly returned: " + doubleRet);
    		    	return;
    			}
    			break;
    			
    		case fer003.FER_VOID:
    			testVoid();
    			break;
    			
    		case fer003.FER_OBJECT:
    			String strRet = testObject();
    			if (strRet != "fer") {
    				System.out.println("Incorrectly returned: " + strRet);
    				return;
    			}
    			break;
    			
    		default:
    			fer003.testPassed = false;
    			return;    			
    	}
    	
    	fer003.testPassed = true;
    }
    
    
    private int testInt() 
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }
    	
    	return fer003.VALUE_INT_DEFAULT;
    }

    private long testLong()
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }

    	return fer003.VALUE_LONG_DEFAULT;
    }

    
    private float testFloat()
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }
        
    	return fer003.VALUE_FLOAT_DEFAULT;
    }

    private double testDouble()
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }
        
    	return fer003.VALUE_DOUBLE_DEFAULT;
    }

    private String testObject()
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }
        
    	return fer003.VALUE_OBJ_DEFAULT;
    }
    
    private void testVoid()
    {
    	synchronized (fer003.runForestRun) {
        	fer003.forestIsRunning = true;
        	fer003.runForestRun.notifyAll();
        }

    	int n = Integer.MAX_VALUE;

        while (keepRunning) {
        	 if (n < 0) {
                 n = Integer.MAX_VALUE - 1;
             } else {
            	 n = Integer.MAX_VALUE;
             }
        }
        
    	return;
    }
}

