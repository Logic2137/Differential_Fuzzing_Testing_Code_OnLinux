
package org.openj9.test.hangTest;
import java.io.IOException;

public class Hang {
    public static void main(String [] args) {
        Hook hook = new Hook();
        System.out.println("adding hook ... ");
        Runtime.getRuntime().addShutdownHook(hook);
        System.out.println("Exiting ... ");
    }

    private static class Hook extends Thread {
        public void run() {
			int i = 0;
			while(i < 10) {
				try {
					System.out.println("Sleep inside Hook ...." + i);
					Thread.sleep(1000);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    }
}
