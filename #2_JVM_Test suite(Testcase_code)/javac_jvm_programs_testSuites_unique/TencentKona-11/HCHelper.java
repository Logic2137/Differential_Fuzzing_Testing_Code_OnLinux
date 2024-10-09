
package gc.hashcode;

import java.util.ArrayList;
import java.util.Random;


public final class HCHelper {

    
    public static final int EVAC_LIST_0 = 0;
    
    public static final int EVAC_LIST_1 = 1;
    
    public static final int EVAC_LIST_2 = 2;
    
    public static final int EVAC_LIST_3 = 3;
    
    public static final int EVAC_LIST_4 = 4;
    
    public static final int EVAC_LIST_5 = 5;
    
    public static final double EVAC_SIZE_0 = 0.50;
    
    public static final double EVAC_SIZE_1 = 0.14;
    
    public static final double EVAC_SIZE_2 = 0.12;
    
    public static final double EVAC_SIZE_3 = 0.10;
    
    public static final double EVAC_SIZE_4 = 0.07;
    
    public static final double EVAC_SIZE_5 = 0.05;

    
    final class AllocObject {
        private byte[] allocatedArray;
        private int hashValue;

        
        AllocObject(int size) {
            allocatedArray = new byte[size];
            hashValue = allocatedArray.hashCode();
        }

        
        int getStoredHashValue() {
            return hashValue;
        }

        
        int getCurrentHashValue() {
            return allocatedArray.hashCode();
        }

        
        int getAllocatedSize() {
            return allocatedArray.length;
        }
    }

    
    final class AllocInfo {
        private long allocatedSize;
        private long numOfAllocedObjs;
        private ArrayList safeList;
        private ArrayList allocList;
        private ArrayList evacList0;
        private ArrayList evacList1;
        private ArrayList evacList2;
        private ArrayList evacList3;
        private ArrayList evacList4;
        private ArrayList evacList5;

        
        AllocInfo() {
            allocatedSize = 0;
            numOfAllocedObjs = 0;
            safeList = new ArrayList();
            allocList = new ArrayList();
            evacList0 = new ArrayList();
            evacList1 = new ArrayList();
            evacList2 = new ArrayList();
            evacList3 = new ArrayList();
            evacList4 = new ArrayList();
            evacList5 = new ArrayList();
        }

        
        public long getAllocatedSize() {
            return allocatedSize;
        }

        
        public void setAllocatedSize(long allocatedSize) {
            this.allocatedSize = allocatedSize;
        }

        
        public long getNumOfAllocedObjs() {
            return numOfAllocedObjs;
        }

        
        public void setNumOfAllocedObjs(long numOfAllocedObjs) {
            this.numOfAllocedObjs = numOfAllocedObjs;
        }

        
        public void incNumOfAllocedObjs() {
            numOfAllocedObjs++;
        }

        
        public void decNumOfAllocedObjs() {
            numOfAllocedObjs--;
        }

        
        public ArrayList getSafeList() {
            return safeList;
        }

        
        public ArrayList getAllocList() {
            return allocList;
        }

        
        public ArrayList getEvacList0() {
            return evacList0;
        }

        
        public ArrayList getEvacList1() {
            return evacList1;
        }

        
        public ArrayList getEvacList2() {
            return evacList2;
        }

        
        public ArrayList getEvacList3() {
            return evacList3;
        }

        
        public ArrayList getEvacList4() {
            return evacList4;
        }

        
        public ArrayList getEvacList5() {
            return evacList5;
        }
    }


    private int minSize;
    private int maxSize;
    private double percentToFill;
    private int allocTrigSize;
    private AllocInfo ai;
    private Random rnd;

    private long sizeLimit0;
    private long sizeLimit1;
    private long sizeLimit2;
    private long sizeLimit3;
    private long sizeLimit4;
    private long sizeLimit5;

    
    public HCHelper(int minSize, int maxSize, long seed,
                    double percentToFill, int allocTrigSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.percentToFill = percentToFill;
        this.allocTrigSize = allocTrigSize;
        ai = new AllocInfo();
        rnd = new Random(seed);

        sizeLimit0 = 0;
        sizeLimit1 = 0;
        sizeLimit2 = 0;
        sizeLimit3 = 0;
        sizeLimit4 = 0;
        sizeLimit5 = 0;
    }

    
    public void setupLists() {
        Runtime r = Runtime.getRuntime();
        long maxMem = r.maxMemory();
        long safeMaxMem = (long) (maxMem * percentToFill);
        sizeLimit0 = (long) (safeMaxMem * EVAC_SIZE_0);
        sizeLimit1 = (long) (safeMaxMem * EVAC_SIZE_1);
        sizeLimit2 = (long) (safeMaxMem * EVAC_SIZE_2);
        sizeLimit3 = (long) (safeMaxMem * EVAC_SIZE_3);
        sizeLimit4 = (long) (safeMaxMem * EVAC_SIZE_4);
        sizeLimit5 = (long) (safeMaxMem * EVAC_SIZE_5);

        
        System.gc();
        allocObjects(ai.getEvacList0(), sizeLimit0);
        System.gc();
        allocObjects(ai.getEvacList1(), sizeLimit1);
        System.gc();
        allocObjects(ai.getEvacList2(), sizeLimit2);
        System.gc();
        allocObjects(ai.getEvacList3(), sizeLimit3);
        System.gc();
        allocObjects(ai.getEvacList4(), sizeLimit4);
        System.gc();
        allocObjects(ai.getEvacList5(), sizeLimit5);
        System.gc();
    }

    private void allocObjects(ArrayList al, long totalSizeLimit) {
        long allocedSize = 0;
        int multiplier = maxSize - minSize;

        while (allocedSize < totalSizeLimit) {
            int allocSize = minSize + (int) (rnd.nextDouble() * multiplier);
            if (allocSize >= totalSizeLimit - allocedSize) {
                allocSize = (int) (totalSizeLimit - allocedSize);
            }

            al.add(new AllocObject(allocSize));
            allocedSize += allocSize;
        }
    }

    
    public void clearList(int listNr) {
        if (listNr < EVAC_LIST_0 || listNr > EVAC_LIST_5) {
            throw new IllegalArgumentException("List to removed bust be "
                    + "between EVAC_LIST_0 and EVAC_LIST_5");
        }

        switch (listNr) {
            case EVAC_LIST_0:
                ai.getEvacList0().clear();
                break;
            case EVAC_LIST_1:
                ai.getEvacList1().clear();
                break;
            case EVAC_LIST_2:
                ai.getEvacList2().clear();
                break;
            case EVAC_LIST_3:
                ai.getEvacList3().clear();
                break;
            case EVAC_LIST_4:
                ai.getEvacList4().clear();
                break;
            case EVAC_LIST_5:
                ai.getEvacList5().clear();
                break;
            default: 
                break;
        }
    }

    
    boolean verifyHashCodes(ArrayList objList) {
        
        for (int i = 0; i < objList.size(); i++) {
            AllocObject tmp = (AllocObject) objList.get(i);
            if (tmp.getStoredHashValue() != tmp.getCurrentHashValue()) {
                
                return false;
            }
        }

        return true;
    }


    
    public boolean verifyHashCodes() {
        return verifyHashCodes(ai.getAllocList())
                && verifyHashCodes(ai.getSafeList())
                && verifyHashCodes(ai.getEvacList0())
                && verifyHashCodes(ai.getEvacList1())
                && verifyHashCodes(ai.getEvacList2())
                && verifyHashCodes(ai.getEvacList3())
                && verifyHashCodes(ai.getEvacList4())
                && verifyHashCodes(ai.getEvacList5());
    }

    
    public void cleanupLists() {
        ai.getAllocList().clear();
        ai.getSafeList().clear();

        ai.getEvacList0().clear();
        ai.getEvacList1().clear();
        ai.getEvacList2().clear();
        ai.getEvacList3().clear();
        ai.getEvacList4().clear();
        ai.getEvacList5().clear();
    }

    
    public long getEvac0Size() {
        return sizeLimit0;
    }

    
    public long getEvac1Size() {
        return sizeLimit1;
    }

    
    public long getEvac2Size() {
        return sizeLimit2;
    }

    
    public long getEvac3Size() {
        return sizeLimit3;
    }

    
    public long getEvac4Size() {
        return sizeLimit4;
    }

    
    public long getEvac5Size() {
        return sizeLimit5;
    }
}
