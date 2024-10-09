

package nsk.jdi.VirtualMachine.redefineClasses;



public class redefineclasses021b {

    redefineclasses021bc obj = new redefineclasses021bc();

    public interface redefineclasses021bi {

        void dummyMethod01();

    }

    public interface redefineclasses021bir {
        void dummyMethod01();

    }

    class redefineclasses021bc implements redefineclasses021bi, redefineclasses021bir {

        public void dummyMethod01() {
        }

    }
}
