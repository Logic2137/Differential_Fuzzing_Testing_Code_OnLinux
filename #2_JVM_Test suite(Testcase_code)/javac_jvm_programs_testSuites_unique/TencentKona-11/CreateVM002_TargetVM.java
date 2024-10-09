

package nsk.jdi.VirtualMachineManager.createVirtualMachine;




public class CreateVM002_TargetVM {

    static final int STATUS_PASSED = 0;
    static final int STATUS_FAILED = 2;
    static final int STATUS_TEMP = 95;


    public static void main (String argv[]) {
        System.exit(STATUS_PASSED + STATUS_TEMP);
    }


} 
