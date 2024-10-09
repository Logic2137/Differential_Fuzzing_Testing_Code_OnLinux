
import java.util.*;

enum ParseResult {
	OPTION_OK ("ok"),
	OPTION_MISSING_VALUE ("missing value"),
	OPTION_UNRECOGNIZED ("unrecognized");

	private String message;
	ParseResult(String msg) {
		message = msg;
	}

	public String toString() {
		return message;
	}
}

public class ActiveIdleTest {
	
	final static String BUSY_PERIOD = "--busy-period";
	final static String IDLE_PERIOD = "--idle-period";

	
	final static String options[] = { BUSY_PERIOD, IDLE_PERIOD };

	
	static long busyPeriod = 30; 
	static long idlePeriod = 300; 

	static int idleToActiveCount = 1;
	static int activeToIdleCount = 1;

	public static void printUsage() {
		System.out.println("Usage: java ActiveIdleTest [" + BUSY_PERIOD + "=<secs>] [" + IDLE_PERIOD + "=<secs>]");
	}

	public static ParseResult parseArgs(String args[]) {
		String name = null;
		String value = null;
		ParseResult parseResult = ParseResult.OPTION_OK;

		for (int i = 0; i < args.length; i++) {
			int j = 0;

			name = args[i];
			value = null;
			if (args[i].indexOf('=') != -1) {
				String optionParts[] = args[i].split("=");

				name = optionParts[0];
				value = optionParts[1];
			}
			for (j = 0; j < options.length; j++) {
				if (name.equals(options[j])) {
					switch (name) {
						case BUSY_PERIOD:
							if (value != null) {
								busyPeriod = Long.parseLong(value);
							} else {
								parseResult = ParseResult.OPTION_MISSING_VALUE;
							}
							break;
						case IDLE_PERIOD:
							if (value != null) {
								idlePeriod = Long.parseLong(value);
							} else {
								parseResult = ParseResult.OPTION_MISSING_VALUE;
							}
							break;
					}
					break;
				}
			}
			if (j == options.length) {
				parseResult = ParseResult.OPTION_UNRECOGNIZED;
			}

			if (parseResult != ParseResult.OPTION_OK) {
				System.out.println("Error in parsing option " + args[i] + " : " + parseResult);
				break;
			}
		}
		return parseResult;
	}

	public static void main(String args[]) {
		ParseResult result = parseArgs(args);
		if (result != ParseResult.OPTION_OK) {
			printUsage();
			return;
		}
		busyLoop(busyPeriod);
		idleLoop(idlePeriod);
		busyLoop(busyPeriod);
	}

	public static void busyLoop(long busyPeriod) {
		System.out.println("Busy looping ... ");
		long startTime = System.nanoTime();
		do {
			ActiveWork.doWork();
		} while ((System.nanoTime() - startTime) / 1000000L < (busyPeriod * 1000));
		System.out.println("Busy loop done");
	}

	public static void idleLoop(long idlePeriod) {
		System.out.println("Idling ... ");
		long startTime = System.nanoTime();
		long sleepTime = idlePeriod * 1000;
		do {
			try {
				Thread.currentThread().sleep(sleepTime);
			} catch (Exception e) {
				
			} finally {
				long elapsed = (System.nanoTime() - startTime) / 1000000L; 
				sleepTime = sleepTime - elapsed;
			}
		} while (sleepTime > 0);
		System.out.println("Idling done");
	}

	static class ActiveWork {
		static List<Long> list;

		public static void doWork() {
			int primeCount = 0;

			list = new ArrayList<>();

			
			for (int i = 0; i < 1000; i++) {
				long number = System.nanoTime() % 1000000;
				list.add(Long.valueOf(number));
			}

			
			for (Long value: list) {
				long counter = 2;
				long val = value.longValue();
				boolean isPrime = true;

				for (counter = 2; counter < val; counter++) {
					if (val % counter == 0) {
						isPrime = false;
					}
				}
				if (isPrime == true) {
					primeCount += 1;
				}
			}
			System.out.println("Prime numbers in the list: " + primeCount);
		}
	}
}

