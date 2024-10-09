

import java.util.*;
import java.io.*;
import java.text.*;


public class Stopwatch {

	private long startTime = -1;
	private long stopTime = -1;
	private boolean running = false;
	private Calendar calendar = Calendar.getInstance();
	TimeZone timeZone = calendar.getTimeZone();
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public Stopwatch start() {
		System.out.println("Test start time: " + df.format(calendar.getTime()) +  " " + timeZone.getDisplayName());
		startTime = System.currentTimeMillis();
		running = true;
		return this;
	}

	public Stopwatch stop() {
		stopTime = System.currentTimeMillis();
		running = false;
		return this;
	}

	
	public long getTimeSpent() {
		if (startTime == -1) {
			return 0;
		}
		if (running) {
			return System.currentTimeMillis() - startTime;
		} else {
			return stopTime - startTime;
		}
	}
}
