import com.sun.tools.attach.VirtualMachine;
import java.io.IOException;

public class AttachSelf {

    public static void main(String[] args) throws Exception {
        String value = System.getProperty("jdk.attach.allowAttachSelf");
        boolean canAttachSelf = (value != null) && !value.equals("false");
        String vmid = "" + ProcessHandle.current().pid();
        VirtualMachine vm = null;
        try {
            vm = VirtualMachine.attach(vmid);
            if (!canAttachSelf)
                throw new RuntimeException("Attached to self not expected");
        } catch (IOException ioe) {
            if (canAttachSelf)
                throw ioe;
        } finally {
            if (vm != null)
                vm.detach();
        }
    }
}
