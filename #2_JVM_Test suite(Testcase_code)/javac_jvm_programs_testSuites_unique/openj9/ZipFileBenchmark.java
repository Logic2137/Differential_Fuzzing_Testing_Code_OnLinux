package j9vm.test.benchmark.zipfile;



import java.io.File;
import java.io.InputStream;
import java.util.zip.*;
import java.util.*;
import java.text.*;

public class ZipFileBenchmark {
	public static void main (String[] args) {
		
		try{
			ZipFile myZip = null;

			long load;
			long open;
			long getentries;
			long fail;
			long succeed;
			long getentriesnewfile = 0;
			long readbytes;
			
			
			
			if (args.length <2){
				System.out.println("ERROR: Missing required arguments !");
				System.out.println("	First argument is jar containing test entries");
				System.out.println("	Second argument is number of times to repeat operations");
				return;
			}
			
			if ((new File(args[0]).canRead() != true)){
				System.out.println("ERROR: cannot read jar file specified !");
				return;
			}
			
			try {
				load = Long.parseLong(args[1]);
			} catch (Exception e){
				System.out.println("ERROR: failed to parse number of times to repeat operations: " + e);
				return;
			}

			
			String zipName = args[0];

			
			try{
				myZip = new ZipFile(zipName);
				myZip.entries();
				ZipEntry theEntry = myZip.getEntry("j9vm/test/benchmark/zipfile/ZipFileBenchmark.class");
				InputStream theStream = myZip.getInputStream(theEntry);
				byte[] data = new byte[10000];
				theStream.read(data,0,theStream.available());
				myZip.close();
			}catch( Exception e){
			}
			
			
			Thread.sleep(5000);
			
			
			long startTime = System.nanoTime();
			for (int i=0;i<load;i++){
				myZip = new ZipFile(zipName);
				myZip.close();
			}
			long endTime = System.nanoTime();
			System.out.println("Number of nanoseconds to repeatedly open/close zip file: " + (endTime-startTime));
			open = endTime-startTime;

			myZip = new ZipFile(zipName);
		
			
			startTime = System.nanoTime();
			for (int i=0;i<load;i++){
				Enumeration entries = myZip.entries();
			}
			endTime = System.nanoTime();
			System.out.println("Number of nanoseconds to repeatedly get entries on open zip: " + (endTime-startTime));
			getentries = endTime-startTime;
			
			
			startTime =0;
			getentriesnewfile = 0;
			for (int i=0;i<load;i++){
				myZip = new ZipFile(zipName);
				startTime = System.nanoTime();
				Enumeration entries = myZip.entries();
				endTime = System.nanoTime();
				getentriesnewfile += endTime-startTime;
				myZip.close();
			}
			System.out.println("Number of nanoseconds to repeatedly get entries new file: " + getentriesnewfile);
			myZip.close();
			
			
			myZip = new ZipFile(zipName);
			startTime = System.nanoTime();
			for (int i=0;i<load;i++){
				myZip.getEntry("9vm/test/benchmark/zipfile/invalid");
			}
			endTime = System.nanoTime();
			System.out.println("Number of nanoseconds to repeatedly lookup invalid entry: " + (endTime-startTime));
			fail = endTime-startTime;
			myZip.close();
		
			
			myZip = new ZipFile(zipName);
			startTime = System.nanoTime();
			for (int i=0;i<load;i++){
				myZip.getEntry("j9vm/test/benchmark/zipfile/ZipFileBenchmark.class");
			}
			endTime = System.nanoTime();
			System.out.println("Number of nanoseconds to repeatedly lookup valid entry: " + (endTime-startTime));
			succeed = endTime-startTime;
			myZip.close();
			
			
			myZip = new ZipFile(zipName);
			startTime = System.nanoTime();
			ZipEntry theEntry = myZip.getEntry("j9vm/test/benchmark/zipfile/ZipFileBenchmark.class");
			byte[] data = new byte[10000];
			for (int i=0;i<load;i++){
				InputStream theStream = myZip.getInputStream(theEntry);
				theStream.read(data,0,theStream.available());
			}
			endTime = System.nanoTime();
			System.out.println("Number of nanoseconds to repeatedly read an entry: " + (endTime-startTime));
			readbytes = endTime-startTime;
			myZip.close();
			
			NumberFormat myf = NumberFormat.getInstance();
			myf.setMinimumIntegerDigits(16);
			myf.setGroupingUsed(false);
			System.out.println("Comparison " + myf.format(open) + " " + myf.format(getentries) + " " + myf.format(getentriesnewfile)+ " " + myf.format(fail) + " " + myf.format(succeed)+ " " + myf.format(readbytes));
			
		} catch(Exception e){
			System.out.println("Unexpected exception:" + e);
			e.printStackTrace();
		}

	}
}
