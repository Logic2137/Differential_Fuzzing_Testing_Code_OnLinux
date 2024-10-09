

import java.util.Set;
import java.util.HashSet;


public class AllModulesCommandTestDebuggee {

    public static void main(String[] args) throws InterruptedException {

        int modCount = ModuleLayer.boot().modules().size();

        
        for (Module mod : ModuleLayer.boot().modules()) {
            String info = String.format("module %s", mod.getName());
            write(info);
        }
        
        write("ready");
        Thread.sleep(Long.MAX_VALUE);
    }

    private static void write(String s) {
        System.out.println(s);
        System.out.flush();
    }

}
