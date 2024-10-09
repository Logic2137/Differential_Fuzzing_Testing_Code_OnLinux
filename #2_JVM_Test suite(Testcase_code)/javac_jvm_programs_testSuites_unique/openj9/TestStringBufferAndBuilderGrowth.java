

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;


public class TestStringBufferAndBuilderGrowth {

public static void main(String[] args) {
	OperatingSystemMXBean opBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	long physicalMemory = opBean.getTotalPhysicalMemorySize();
	System.out.println("Machine has physical memory " + physicalMemory + " bytes or " + (physicalMemory >> 20) + " MB or " + (physicalMemory >> 30) + " GB");
	
	long limit = 8193L << 20; 
	if (physicalMemory < limit) {
		
		System.out.println("Not enough resource to run test.");
		return;
	}

	char[] cbuf = new char[Integer.MAX_VALUE / 32];

	
	
	
	
	StringBuffer sbuf = new StringBuffer((Integer.MAX_VALUE / 2) + 1);
	
	
	for (int i = 0; i < 17; i++) {
		try {
 	 		sbuf.append(cbuf);
		} catch (OutOfMemoryError e) {
			System.out.println("OOM StringBuffer occurred iteration " + i);
			return;
		}
	}
	int sbufCapacity = sbuf.capacity();
	
	
	sbuf = null;

	
	StringBuilder sbld = new StringBuilder((Integer.MAX_VALUE / 2) + 1);
	for (int i = 0; i < 17; i++) {
		try {
 	 		sbld.append(cbuf);
		} catch (OutOfMemoryError e) {
			System.out.println("OOM StringBuilder occurred iteration " + i);
			return;
		}
	}
	int sbldCapacity = sbld.capacity();

	System.out.println("StringBuffer capacity=" + sbufCapacity + " StringBuilder capacity=" + sbldCapacity);
}
}
