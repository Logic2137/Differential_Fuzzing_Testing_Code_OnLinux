



import java.security.Provider;

public class GetServiceRace {

    private static final Provider testProvider;
    static {
        testProvider = new Provider("MyProvider", 1.0, "test") {
        };
        testProvider.put("CertificateFactory.Fixed", "MyCertificateFactory");
    }

    private static final int NUMBER_OF_RETRIEVERS = 3;
    private static final int TEST_TIME_MS = 1000;

    public static boolean testFailed = false;

    public static void main(String[] args) throws Exception {
        Updater updater = new Updater();
        updater.start();
        Retriever [] retrievers = new Retriever[NUMBER_OF_RETRIEVERS];
        for (int i=0; i<retrievers.length; i++) {
            retrievers[i] = new Retriever();
            retrievers[i].start();
        }
        Thread.sleep(TEST_TIME_MS);
        System.out.println("Interrupt");
        updater.interrupt();
        updater.join();
        for (int i=0; i<retrievers.length; i++) {
            retrievers[i].interrupt();
            retrievers[i].join();
        }
        System.out.println("Done");
        if (testFailed) {
            throw new Exception("Test Failed");
        }
        System.out.println("Test Passed");
    }

    private static class Updater extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                testProvider.put("CertificateFactory.Added", "MyCertificateFactory");
            }
            System.out.println("Updater stopped");
        }
    }

    private static class Retriever extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                Provider.Service service = testProvider.getService("CertificateFactory", "Fixed");
                if (service == null) {
                    if (!testFailed) {
                        System.err.println("CertificateFactory.Fixed is NULL");
                        testFailed = true;
                    }
                } else {
                    
                }
            }
            System.out.println("Retriever stopped");
        }
    }
}
