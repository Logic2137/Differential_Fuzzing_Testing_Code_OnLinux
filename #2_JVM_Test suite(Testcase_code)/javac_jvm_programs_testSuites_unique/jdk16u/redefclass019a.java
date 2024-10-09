



package nsk.jvmti.RedefineClasses;

class redefclass019a extends Thread {

    public void run() {
        chain1();
        return; 
    }

    public void chain1() {
        int localInt1 = 2;
        int localInt2 = 3333;
        chain2();
        return; 
    }

    public void chain2() {
        chain3();
        return; 
    }

    public void chain3() {
        checkPoint();
        return; 
    }

    
    void checkPoint() {
        new Integer("4");
        return;
    }
}
