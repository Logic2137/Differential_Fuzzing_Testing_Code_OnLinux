

 
import javax.management.MBeanOperationInfo;

public class MBeanOperationInfoImpactRangeTest {

    private void checkInRange(int impact) {
        int impactValue;

        System.out.println("checking that no exception is thrown when a "
                + "value in range is passed, impact value is :" + impact );
        MBeanOperationInfo mbi = new MBeanOperationInfo("IRC", "impact Range"
                + " check", null, null, impact);
        impactValue = mbi.getImpact();
        if(impactValue != impact)
            throw new RuntimeException("unexpected impact value :" + impactValue);
        System.out.println("given value is :" + impactValue);
        System.out.println("Success no exception thrown");
        System.out.println(mbi.toString());

    }

    private void checkOutOfRange(int impact) {
        int impactValue;

        try {
            System.out.println("checking that exception is thrown when a value"
                    + " out of range is passed, impact value is :" + impact);
            MBeanOperationInfo mbi = new MBeanOperationInfo("IRC", "impact Range"
                    + " check", null, null, impact);
            impactValue = mbi.getImpact();
            System.out.println("IllegalArgumentException not thrown"
                    + " when a value out of range is passed ,"
                    + " given value is :" + impactValue);
            throw new RuntimeException("Test failed !!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException thrown as expected, "
                    + "illegal value given as impact :" + impact);
            System.out.println("success");
        }

    }

    public static void main(String Args[]) {

        
        
        MBeanOperationInfoImpactRangeTest impactRangeTest = new MBeanOperationInfoImpactRangeTest();

        impactRangeTest.checkInRange(MBeanOperationInfo.INFO);
        impactRangeTest.checkInRange(MBeanOperationInfo.ACTION);
        impactRangeTest.checkInRange(MBeanOperationInfo.ACTION_INFO);
        impactRangeTest.checkInRange(MBeanOperationInfo.UNKNOWN);
        impactRangeTest.checkOutOfRange(-1);
        impactRangeTest.checkOutOfRange(4);

        System.out.println("Test Passed");


    }
}
