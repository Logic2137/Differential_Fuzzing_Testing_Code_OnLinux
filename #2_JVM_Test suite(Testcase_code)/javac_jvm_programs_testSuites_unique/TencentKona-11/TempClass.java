
package nsk.jvmti.scenarios.hotswap.HS204.hs204t004;


public class TempClass {
        private int x;
        public TempClass(int p) {
                this.x= p;
        }
        public TempClass doThis() {
                return new TempClass(x+1);
        }
}
