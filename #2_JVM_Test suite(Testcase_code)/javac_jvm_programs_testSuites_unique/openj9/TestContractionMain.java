
package com.ibm.tests.garbagecollector;


public class TestContractionMain
{
	public static Object[][] _array;
	public static final int SIZE = 500;

	
	public static void main(String[] args)
	{
		boolean noContract = _match(args, "--noContract");
		boolean slowContract = _match(args, "--slowContract");
		boolean fastContract = _match(args, "--fastContract");
		boolean verbose = _match(args, "--verbose");

		if (1 == ((noContract ? 1 : 0) + (slowContract ? 1 : 0) + (fastContract ? 1 : 0)))
		{
			if (verbose)
			{
				System.out.println("Starting verbose test run...");
			}
			boolean pass = false;
			Runtime runtime = Runtime.getRuntime();
			long initMem = runtime.totalMemory();
			_array = new Object[SIZE][];
			for (int i = 0; i < _array.length; i++)
			{
				_array[i] = new Object[SIZE];
				for (int j = 0; j < _array[i].length; j++)
				{
					_array[i][j] = new Object();
				}
			}
			long highWaterMem = runtime.totalMemory();
			_array = null;
			System.gc();
			System.gc();
			System.gc();
			long tempMem = runtime.totalMemory();
			System.gc();
			System.gc();
			System.gc();
			long tempMem2 = runtime.totalMemory();

			if (verbose)
			{
				System.out.println("Test complete.  Memory stats:  (initial = " + initMem + ", high water = " + highWaterMem + ", after first GC set = " + tempMem + ", after second GC set = " + tempMem2);
			}

			
			if (highWaterMem > initMem)
			{
				if (noContract)
				{
					if (verbose)
					{
						System.out.print("Test noContract (totalMemory should be unchanged across System.GC() calls) completed...");
					}
					pass = (highWaterMem == tempMem) && (highWaterMem == tempMem2);
				}
				else if (slowContract)
				{
					if (verbose)
					{
						System.out.print("Test slowContract (totalMemory should slowly reduce across System.GC() calls) completed...");
					}
					pass = (tempMem2 < tempMem) && (tempMem < highWaterMem);
				}
				else if (fastContract)
				{
					if (verbose)
					{
						System.out.print("Test noContract (fastContract should change only after the first System.GC() call) completed...");
					}
					pass = (tempMem < highWaterMem) && (tempMem == tempMem2);
				}

				if (pass)
				{
					System.out.println("PASS");
					System.exit(0);
				}
				else
				{
					System.out.println("FAIL");
					System.exit(1);
				}
			}
			else
			{
				System.err.println("Test error:  memory usage did not increase over run");
				System.exit(3);
			}
		}
		else
		{
			System.err.println("Specify exactly one of --noContract, --slowContract, or --fastContract");
			System.exit(2);
		}
	}

	private static boolean _match(String[] args, String match)
	{
		boolean toRet = false;

		for (int i = 0; !toRet && (i < args.length); i++)
		{
			toRet = match.equals(args[i]);
		}
		return toRet;
	}
}
