

package nsk.share.gc;

import java.io.PrintStream;

public class GCParams {
        private String garbageProducerId;
        private String garbageProducer1Id;
        private String memoryStrategyId;
        private String lockersId;

        public final String getGarbageProducerId() {
                return garbageProducerId;
        }

        public final void setGarbageProducerId(String garbageProducerId) {
                this.garbageProducerId = garbageProducerId;
        }

        public final String getGarbageProducer1Id() {
                return garbageProducer1Id;
        }

        public final void setGarbageProducer1Id(String garbageProducer1Id) {
                this.garbageProducer1Id = garbageProducer1Id;
        }

        public final String getMemoryStrategyId() {
                return memoryStrategyId;
        }

        public final void setMemoryStrategyId(String memoryStrategyId) {
                this.memoryStrategyId = memoryStrategyId;
        }

        public final String getLockersId() {
                return lockersId;
        }

        public final void setLockersId(String lockersId) {
                this.lockersId = lockersId;
        }

        public void parseCommandLine(String[] args) {
                if (args == null)
                        return;
                for (int i = 0; i < args.length; ++i) {
                        if (args[i].equals("-gp"))
                                garbageProducerId = args[++i];
                        else if (args[i].equals("-gp1"))
                                garbageProducer1Id = args[++i];
                        else if (args[i].equals("-ms"))
                                memoryStrategyId = args[++i];
                        else if (args[i].equals("-lockers"))
                                lockersId = args[++i];
                }
                printConfig(System.out);
        }

        public void prinUsage() {
        }

        public void printConfig(PrintStream out) {
        }

        private static GCParams instance;

        public static GCParams getInstance() {
                synchronized (GCParams.class) {
                        if (instance == null)
                                instance = new GCParams();
                        return instance;
                }
        }

        public static void setInstance(GCParams gcParams) {
                synchronized (GCParams.class) {
                        instance = gcParams;
                }
        }
}
