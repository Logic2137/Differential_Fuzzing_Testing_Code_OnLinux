
package j9vm.test.daemon;


public class Daemon extends java.lang.Thread {
	public Daemon () {
		setDaemon( true );
		setPriority( 3 );
	}

	public void run() {
		
		while(true) {
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
			}
		}
	}

}
