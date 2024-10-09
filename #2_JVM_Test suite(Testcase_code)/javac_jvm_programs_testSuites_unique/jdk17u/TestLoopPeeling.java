
package compiler.loopopts;

public class TestLoopPeeling {

    public int[] array = new int[100];

    public static void main(String[] args) {
        TestLoopPeeling test = new TestLoopPeeling();
        try {
            test.testArrayAccess1(0, 1);
            test.testArrayAccess2(0);
            test.testArrayAccess3(0, false);
            test.testArrayAllocation(0, 1);
        } catch (Exception e) {
        }
    }

    public void testArrayAccess1(int index, int inc) {
        int storeIndex = -1;
        for (; index < 10; index += inc) {
            if (inc == 42)
                return;
            if (storeIndex > 0 && array[storeIndex] == 42)
                return;
            if (index == 42) {
                array[storeIndex] = 1;
                return;
            }
            storeIndex++;
        }
    }

    public int testArrayAccess2(int index) {
        int storeIndex = Integer.MIN_VALUE;
        for (; index < 10; ++index) {
            if (index == 42) {
                return array[storeIndex - 1];
            }
            storeIndex = 0;
        }
        return array[42];
    }

    public int testArrayAccess3(int index, boolean b) {
        int storeIndex = Integer.MIN_VALUE;
        for (; index < 10; ++index) {
            if (b) {
                return 0;
            }
            if (index == 42) {
                return array[storeIndex - 1];
            }
            storeIndex = 0;
        }
        return array[42];
    }

    public byte[] testArrayAllocation(int index, int inc) {
        int allocationCount = -1;
        byte[] result;
        for (; index < 10; index += inc) {
            if (inc == 42)
                return null;
            if (index == 42) {
                result = new byte[allocationCount];
                return result;
            }
            allocationCount++;
        }
        return null;
    }
}
