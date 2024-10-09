
package j9vm.runner;

import java.io.*;


public class OutputCollector extends Thread {
	private BufferedInputStream myStream;
	private ByteArrayOutputStream bout;

	public OutputCollector(BufferedInputStream myStream) {
		this.myStream = myStream;
		this.bout = new ByteArrayOutputStream();
	}

	public void run() {
		byte [] buf = new byte[100];
		try {
			int length = this.myStream.read(buf);
			while (length >= 0)  {
				System.out.write(buf, 0, length);
				bout.write(buf, 0, length);
				length = this.myStream.read(buf);
			}
		} catch(IOException e) {
			System.out.println("------------------- BEGIN bogus output from test? ----------------");
			e.printStackTrace();
			System.out.println("------------------- END bogus output from test? ----------------");		
		}
	}

	public byte[] getOutputAsByteArray() {
		return bout.toByteArray();
	}
}
