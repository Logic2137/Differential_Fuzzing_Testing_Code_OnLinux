package com.ibm.jvmti.tests.decompResolveFrame;




public class fThread extends Thread
{
	private String myName;
	private int count = 1;

	public fThread(String theName)
	{
		this.myName = theName;
	}

	public fThread(){

	}

	public void run()
	{
		while(true)
		{
			this.GoGorun();
		}
	}

	public void GoGorun()
	{
		
		for (int q=0; q<100; q++)
		{
			method1();
		}
		

	}

	public void method9()
	{
		

		
		for (int n=0; n<10; n++)
		{
			String myString[] = new String[20];
			
			
			for (int l=0; l<10; l++)
			{
				SmallObject smallObj1 = new SmallObject();   
				String moreString[] = new String[200];
			}
		}

		if(this.getName().startsWith("Ex"))
		{
			int size = 10;
			SmallObject[] smallObjectsArray = new SmallObject[size];
			smallObjectsArray[size+1] = new SmallObject();
		}
	}

	public void method8()
	{
		int size = 10;				
		int wrongSize = 20;

		SmallObject[] smallObjects = new SmallObject[size];

		try
		{
			for(int i = 0; i < wrongSize; i++)
			{
				smallObjects[i] = new SmallObject(); 
			}  							
		}
		catch(ArrayIndexOutOfBoundsException ex)
		{
			
		}

		method9();
	}

	public void method7()
	{
		method8();
	}

	public void method6()
	{
		method7();
	}


	public void method5()
	{
		method6();
	}

	public void method4()
	{
		method5();
	}

	public void method3()
	{
		method4();
	}

	public void method2()
	{
		method3();
	}

	public void method1()
	{
		method2();
	}

	public void logMessage(String message)
	{
		java.text.NumberFormat formatter = java.text.NumberFormat.getIntegerInstance();
	    formatter.setMinimumIntegerDigits(2);
	    java.util.Calendar c = java.util.Calendar.getInstance();
		System.out.println(formatter.format(c.get(java.util.Calendar.HOUR_OF_DAY)) + ":" + formatter.format(c.get(java.util.Calendar.MINUTE)) + ":" + formatter.format(c.get(java.util.Calendar.SECOND)) + " >> " + myName + " :: " + message);
	}
}


class SmallObject
{
	void foo()
	{
	}

	static void boo()
	{
	}
}
