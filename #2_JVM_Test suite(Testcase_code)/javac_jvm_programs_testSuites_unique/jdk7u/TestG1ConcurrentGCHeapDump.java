


import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.GarbageCollectorMXBean;

import sun.management.ManagementFactoryHelper;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;

import java.io.IOException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class TestG1ConcurrentGCHeapDump {

  private static final String HOTSPOT_BEAN_NAME =
    "com.sun.management:type=HotSpotDiagnostic";

  private static final String G1_OLD_BEAN_NAME =
    "java.lang:type=GarbageCollector,name=G1 Old Generation";

  private static MBeanServer server = ManagementFactory.getPlatformMBeanServer();

  private static void dumpHeap() throws IOException {
    HotSpotDiagnosticMXBean hotspot_bean =
      ManagementFactory.newPlatformMXBeanProxy(server,
          HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);

    Path dir = Files.createTempDirectory("JDK-8038925_");
    String file = dir + File.separator + "heapdump";
    hotspot_bean.dumpHeap(file, false);
    Files.delete(Paths.get(file));
    Files.delete(dir);
  }

  private static void verifyNoFullGC() throws IOException {
    GarbageCollectorMXBean g1_old_bean =
      ManagementFactory.newPlatformMXBeanProxy(server,
          G1_OLD_BEAN_NAME, GarbageCollectorMXBean.class);

    if (g1_old_bean.getCollectionCount() != 0) {
      throw new RuntimeException("A full GC has occured, this test will not work.");
    }
  }

  public static void main(String[] args) throws IOException {
    HotSpotDiagnosticMXBean diagnostic = ManagementFactoryHelper.getDiagnosticMXBean();
    VMOption option = diagnostic.getVMOption("UseG1GC");
    if (option.getValue().equals("false")) {
      System.out.println("Skipping this test. It is only a G1 test.");
      return;
    }

    
    ArrayList<List<Integer>> arraylist = new ArrayList<List<Integer>>();
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 100; j++) {
        LinkedList<Integer> li = new LinkedList<Integer>();
        arraylist.add(li);
        for (int k = 0; k < 10000; k++) {
          li.add(k);
        }
      }
      arraylist = new ArrayList<List<Integer>>();
      System.gc();
    }
    
    dumpHeap();
    
    verifyNoFullGC();
  }
}
