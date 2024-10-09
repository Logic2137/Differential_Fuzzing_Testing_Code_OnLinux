import com.sun.tools.attach.VirtualMachine;

public class GetPackageBootLoaderChildLayer {

    public static void main(String[] args) throws Exception {
        ModuleLayer.boot().findModule("java.management").ifPresent(m -> {
            throw new RuntimeException("java.management loaded!!!");
        });
        String vmid = "" + ProcessHandle.current().pid();
        VirtualMachine vm = VirtualMachine.attach(vmid);
        vm.startLocalManagementAgent();
        Class<?> clazz = Class.forName("javax.management.MXBean");
        if (clazz.getModule().getLayer() == ModuleLayer.boot())
            throw new RuntimeException("Module is in boot layer!!!");
        ClassLoader loader = clazz.getClassLoader();
        if (loader != null)
            throw new RuntimeException("Unexpected class loader: " + loader);
        Package p = clazz.getPackage();
        if (!p.getName().equals("javax.management"))
            throw new RuntimeException("Unexpected package " + p);
    }
}
