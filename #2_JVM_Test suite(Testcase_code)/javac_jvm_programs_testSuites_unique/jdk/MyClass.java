
package nsk.jvmti.scenarios.hotswap.HS104.hs104t001;
public class MyClass {
        private String message;
        private int state;
        private int size;
        public MyClass(String message, int size) {
                this.message = message;
                state=0;
        }
        public void doThis() {
                for(int i=0; i < size; i++) {
                        System.out.println(" .... Message "+message);
                        state++;
                }
        }
        public int getState() {
                return state;
        }
}
