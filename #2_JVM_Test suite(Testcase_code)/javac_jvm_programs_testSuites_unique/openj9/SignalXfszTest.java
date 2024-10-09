package j9vm.test.sigxfsz;



import static java.lang.System.*;
import java.io.*;

public class SignalXfszTest {

	public static void main(String[] args) throws IOException {
		System.out.println("Starting SignalXfszTest");
		if (1 == args.length) {
			File file = new File(args[0]);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				for (int i = 0; i < 204900; i++) {
					fos.write(new byte[10000]);
				}
				out.println("File is written successfully");
			} catch (Throwable e) {
				System.err.println("Failed to write the file: " + args[0]);
				e.printStackTrace();
			} finally {
				fos.close();
				file.delete();
			}
		} else {
			System.err.println("Usage:  SignalXfszTest <path of the file to be written out>");
			System.exit(-1);
		}
	}
}
