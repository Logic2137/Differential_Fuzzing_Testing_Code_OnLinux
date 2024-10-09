
package vm.share.process;

public interface StreamListener {

    public void onStart();

    public void onRead(String line);

    public void onFinish();

    public void onException(Throwable e);
}
