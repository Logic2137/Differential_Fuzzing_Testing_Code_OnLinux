package j9vm.test.benchmark.zipfile;



import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.jar.*;


public class ClassloadingBenchmark {
	
	protected class ZipRecord {
		protected HashMap zipRecords = new HashMap(); 

		protected ZipRecord(ZipFile f) {
			Enumeration zipFileEnum = f.entries();
			ZipEntry zipEntry = null;
			String zipEntryName = null;
			String className = null;

			while (zipFileEnum.hasMoreElements()) {
				zipEntry = (ZipEntry) zipFileEnum.nextElement();
				zipEntryName = zipEntry.getName();

				
				if (!zipEntryName.endsWith(".class")){
					continue;
				}
			
				className =	zipEntryName.substring(0, zipEntryName.length() - 6);
				className = className.replace('/', '.');
				if (className.startsWith("j9vm.test.benchmark.zipfile.testclasses")){
					zipRecords.put(className, this);
				}
			}
		}
		
		public int getCount(){
			return zipRecords.size();
		}
		
		public Iterator iterator(){
			return zipRecords.keySet().iterator();
		}
	}

	
	public static void main(String[] args) {
		ClassloadingBenchmark runner = new ClassloadingBenchmark();
		runner.run(args);
	}
	
	public void run(String[] args){
		try {
			
			
			if (args.length <1){
				System.out.println("ERROR: Missing required arguments !");
				System.out.println("	First argument is jar containing test entries");
				return;
			}
			
			if ((new File(args[0]).canRead() != true)){
				System.out.println("ERROR: cannot read jar file specified !");
				return;
			}
			
			
			ZipRecord record = new ZipRecord(new ZipFile(args[0]));
			System.out.println("Loaded " + args[0] + " which contains " + record.getCount() + " test classes");
		
			
			System.nanoTime();
			this.getClass().isAssignableFrom(args[0].getClass());
			Iterator classIterWarmup = record.iterator();
			classIterWarmup.hasNext();
			classIterWarmup.next();
			
			
			Thread.sleep(5);
						
			Iterator classIter = record.iterator();
	
			long startTime = System.nanoTime();
			while (classIter.hasNext()) {
				String className = (String) classIter.next();
				Class.forName(className);
			}
			long endTime = System.nanoTime();
			
			System.out.println("Took " + (endTime-startTime) + " nanoseconds to read in " + record.getCount() + " classes");
		} catch (Exception e){
			System.out.println("Unexpected exception:" + e);
			e.printStackTrace();
		}
	}
}
