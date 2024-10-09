
package gc.huge.quicklook.largeheap.MemOptions;

public class MemStat {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max memory   : " + runtime.maxMemory());
        System.out.println("Total memory : " + runtime.totalMemory());
        System.out.println("Free memory  : " + runtime.freeMemory());
        System.exit(0);
    }
}
