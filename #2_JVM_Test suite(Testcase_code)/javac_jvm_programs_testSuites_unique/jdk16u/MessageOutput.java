
package vm.share.process;

public interface MessageOutput {
        public void start();
        public void send(String msg);
        public void finish();
}
