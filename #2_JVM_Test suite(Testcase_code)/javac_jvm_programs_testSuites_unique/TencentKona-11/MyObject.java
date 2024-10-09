
package nsk.jvmti.scenarios.hotswap.HS202.hs202t001;
public class MyObject extends Object {
        private String name="NO NAME";
        private int age=100;
        private boolean updated = false;
        private String phone="PHONE NUMBER ";
        private boolean stop=false;

        public String toString() {
                return ("[ name="+name+", age="+age+",phone="+phone+"]");
        }

        public int hasCode() {
                return name.hashCode();
        }

        public synchronized  void addAge(int i) throws InterruptedException {
                wait(100);
                age+=i;
                updated =true;
                notifyAll();
        }

        public synchronized int getAge() throws InterruptedException  {
                wait(100);
                updated = false;
                notifyAll();
                return age;
        }
        public synchronized boolean isStopped() {
                return stop;
        }
        public synchronized void stop(boolean bool) {
                stop =bool;
        }
        public boolean isUpdated() {
                return updated;
        }

}
