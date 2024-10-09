

package com.oracle.java.testlibrary;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

public final class StreamPumper implements Runnable {

  private static final int BUF_SIZE = 256;

  private final OutputStream out;
  private final InputStream in;

  
  public StreamPumper(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  
  @Override
  public void run() {
    int length;
    InputStream localIn = in;
    OutputStream localOut = out;
    byte[] buffer = new byte[BUF_SIZE];

    try {
      while (!Thread.interrupted() && (length = localIn.read(buffer)) > 0) {
        localOut.write(buffer, 0, length);
      }
    } catch (IOException e) {
      
      e.printStackTrace();
    } finally {
      try {
        localOut.flush();
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
