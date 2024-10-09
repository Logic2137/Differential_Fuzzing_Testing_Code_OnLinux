
package com.ibm.jvmti.tests.getHeapFreeTotalMemory;

public class ghftm001 
{
	native static long getHeapFreeMemory();
	native static long getHeapTotalMemory();
	native static int getCycleStartCount();
	native static int getCycleEndCount();
	
	private final static int MAX_ALLOC_MBYTES = 2 * 1024; 
	private final static int MAX_GC_CYCLE_SECONDS = 30;
	
	public boolean testGetHeapFreeTotalMemory()
	{
		long heapTotal = getHeapTotalMemory();
		long heapFree = getHeapFreeMemory();
		if (heapTotal == -1 || heapFree == -1) {
			System.out.println("Error: getHeapTotalMemory() returned " + heapTotal + " getHeapFreeMemory() returned " + heapFree);
			return false;
		}
		
		if (heapFree > heapTotal) {
			System.out.println("Error: heapFree > heapTotal: " + heapFree + " > " + heapTotal);
			return false;
		}
		
		return true;
	}
	
	public boolean testGarbageCollectionCycleEvent()
	{
		int allocatedMbytes = 0;
		int sleptSeconds = 0;
		while (true) {
			if (getCycleStartCount() != 0) {
				
				if (getCycleEndCount() != 0) {
					
					return true;
				} else if (sleptSeconds < MAX_GC_CYCLE_SECONDS) {
					
					try {
						Thread.sleep(1000);
						sleptSeconds += 1;
					} catch (InterruptedException e) {
						
					}
				} else {
					System.out.println("Error: GC Cycle end event not received within " + MAX_GC_CYCLE_SECONDS + "s");
					return false;
				}
			} else if (allocatedMbytes < MAX_ALLOC_MBYTES) {
				
				char myArray[] = new char[1024*1024]; 
				myArray[0] = 'A'; 
				myArray = null;
				allocatedMbytes += 1;
			} else {
				System.out.println("Error: GC Cycle start event not received after allocating " + MAX_ALLOC_MBYTES +"MB");
				return false;
			}
		}
	}
	
	public String helpGetHeapFreeTotalMemory()
	{
		return "Tests calling the getHeapFree/TotalMemory through the JVMTI extensions";
	}
	
	public String helpGarbageCollectionCycleEvent()
	{
		return "Generates garbage until a cycle event has been sent to JVMTI, or until we give up";
	}
}
