package com.ibm.j9.benchmarks.adapt;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Workers {
	private int threadCount;
	private int iterationCount;
	private byte[] data;

	public MD5Workers(int threadCount, int iterationCount, int bufferSize) {
		this.threadCount = threadCount;
		this.iterationCount = iterationCount;
		data = new byte[bufferSize];
	}

	public BenchJob[] run() throws NoSuchAlgorithmException, InterruptedException {
		BenchJob[] jobs = new BenchJob[threadCount];
		Thread[] jobThreads = new Thread[threadCount];
		for (int i = 0; i < jobs.length; i++) {
			jobs[i] = new BenchJob(data, iterationCount);
			jobs[i].setJobsToObserve(jobs);
			jobThreads[i] = new Thread(jobs[i]);
		}
		for (int i = 0; i < threadCount; i++) {
			jobThreads[i].start();
		}
		for (int i = 0; i < threadCount; i++) {
			jobThreads[i].join();
		}
		return jobs;
	}

	public static String prettify(long val) {
		return String.format("%.4f seconds", val / 1000000000.0);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
		int processorCount = Runtime.getRuntime().availableProcessors(); 

		
		int threadCount = processorCount * 2; 
		int iterationCount = 10000;
		int bufferSize = 1000000;

		if (args.length > 0) {
			String arg = args[0];
			if (args[0].endsWith("n")) {
				arg = arg.substring(0, arg.length() - 1);
				threadCount = processorCount * Integer.valueOf(arg);
			} else {
				threadCount = Integer.valueOf(arg);
			}
		}

		if (args.length > 1) {
			iterationCount = Integer.valueOf(args[1]);
		}

		if (args.length > 2) {
			bufferSize = Integer.valueOf(args[2]);
		}

		System.out.println("Starting benchmark MD5Workers with parameters:\n");
		System.out.printf("  threadCount=%d iterationCount=%d bufferSize=%d\n\n", threadCount, iterationCount, bufferSize);

		long startTime, endTime;

		MD5Workers warmupBench = new MD5Workers(4, 250, bufferSize);

		System.out.println("Performing warmup runs...");
		
		startTime = System.nanoTime();
		warmupBench.run();
		warmupBench.run();
		endTime = System.nanoTime();

		System.out.println("Warmup time used = " + prettify(endTime - startTime));
		System.out.println();

		MD5Workers bench = new MD5Workers(threadCount, iterationCount, bufferSize);

		System.out.println("Starting benchmark run...");

		startTime = System.nanoTime();
		BenchJob[] jobs = bench.run();
		endTime = System.nanoTime();

		for (int i = 0; i < jobs.length; i++) {
			System.out.println("  Job " + i + " wins = " + jobs[i].getNumberOfWins());
		}

		System.out.println("Benchmark time used = " + prettify(endTime - startTime));
	}

	private static class BenchJob implements Runnable {
		private byte[] data;
		private MessageDigest md5;
		private int iters;
		private int progress;
		private int wins;
		private BenchJob[] jobsToObserve;

		public BenchJob(byte[] data, int iters) throws NoSuchAlgorithmException {
			this.data = data;
			this.iters = iters;
			md5 = MessageDigest.getInstance("MD5");
		}

		public int getProgress() {
			return progress;
		}

		public int getNumberOfWins() {
			return wins;
		}

		public void setJobsToObserve(BenchJob[] jobsToObserve) {
			this.jobsToObserve = jobsToObserve;
		}

		private void doWork() {
			synchronized (this) {
				md5.reset();
				md5.update(data);
				if (md5.digest().length == 16) {
					progress++;
				}
			}
		}

		private void observeOtherThreads() {
			if (jobsToObserve != null) {
				int winCount = 0;
				for (BenchJob job : jobsToObserve) {
					synchronized (job) {
						if (job.getProgress() <= progress) {
							winCount++;
						}
					}
				}
				if (winCount == jobsToObserve.length) {
					wins++;
				}
			}
		}

		public void run() {
			while (progress < iters) {
				doWork();
				observeOtherThreads();
			}
		}
	}
}
