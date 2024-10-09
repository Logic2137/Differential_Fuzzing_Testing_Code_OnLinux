public class HandshakeSuspendExitTest implements Runnable {

    static Thread[] _suspend_threads = new Thread[16];

    static volatile boolean _exit_now = false;

    static java.util.concurrent.Semaphore _sem = new java.util.concurrent.Semaphore(0);

    @Override
    public void run() {
        _sem.release();
        while (!_exit_now) {
            for (int i = 0; i < _suspend_threads.length - 2; i++) {
                if (Thread.currentThread() != _suspend_threads[i]) {
                    _suspend_threads[i].suspend();
                    _suspend_threads[i].resume();
                }
            }
        }
        _sem.release();
    }

    public static void main(String... args) throws Exception {
        HandshakeSuspendExitTest test = new HandshakeSuspendExitTest();
        for (int i = 0; i < _suspend_threads.length; i++) {
            _suspend_threads[i] = new Thread(test);
        }
        for (int i = 0; i < _suspend_threads.length; i++) {
            _suspend_threads[i].start();
        }
        for (Thread thr : _suspend_threads) {
            _sem.acquire();
        }
        Thread[] exit_threads = new Thread[128];
        for (int i = 0; i < exit_threads.length; i++) {
            exit_threads[i] = new Thread();
            exit_threads[i].start();
        }
        for (Thread thr : exit_threads) {
            thr.suspend();
        }
        for (Thread thr : exit_threads) {
            thr.resume();
        }
        _exit_now = true;
        int waiting = _suspend_threads.length;
        do {
            for (Thread thr : _suspend_threads) {
                thr.resume();
            }
            while (_sem.tryAcquire()) {
                --waiting;
            }
        } while (waiting > 0);
        for (Thread thr : _suspend_threads) {
            thr.join();
        }
        for (Thread thr : exit_threads) {
            thr.join();
        }
    }
}
