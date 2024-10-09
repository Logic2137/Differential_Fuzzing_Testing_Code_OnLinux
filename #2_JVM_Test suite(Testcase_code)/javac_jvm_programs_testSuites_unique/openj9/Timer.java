

package jit.test.vich.utils;

public class Timer {
	private long start_time;
	private long elapsed_time;
	private long total_time = 0L;


public long delta ( ) {
	return elapsed_time;
}
public void mark ( ) {
	
	elapsed_time = System.currentTimeMillis () - start_time;
	total_time += elapsed_time;
	return;
}

public void reset ( ) { 
	start_time = System.currentTimeMillis (); 
	return; 
}

public long totalTime ( ) {
	return total_time;
}
}
