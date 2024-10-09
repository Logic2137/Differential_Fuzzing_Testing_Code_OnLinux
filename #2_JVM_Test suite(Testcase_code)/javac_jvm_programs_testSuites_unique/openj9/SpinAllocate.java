
package com.ibm.tests.garbagecollector;


public class SpinAllocate
{
	public static Object _objectHolder;

	
	public static void main(String[] args)
	{
		if (1 == args.length)
		{
			int secondsToSpin = Integer.parseInt(args[0]);

			if ((secondsToSpin >= 1) && (secondsToSpin <= 60))
			{
				long finishTime = System.currentTimeMillis() + (secondsToSpin * 1000);
				while (System.currentTimeMillis() < finishTime)
				{
					_objectHolder = new Object();
				}
				System.out.println("Test ran to completion");
			}
			else
			{
				System.err.println("Invalid option given for seconds (" + secondsToSpin + ").  Value given must be in the range [1-60].");
				System.exit(2);
			}
		}
		else
		{
			System.err.println("Missing argument for test run time.  Please specify the number of seconds desired for the test run (in the range [1-60]).");
			System.exit(1);
		}
	}
}
