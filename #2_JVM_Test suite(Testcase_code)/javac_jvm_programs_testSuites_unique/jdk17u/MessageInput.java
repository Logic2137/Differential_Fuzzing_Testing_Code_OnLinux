
package vm.share.process;

import java.util.List;

public interface MessageInput {

    public boolean waitForStart(long timeout) throws InterruptedException;

    public boolean waitForMessage(long timeout) throws InterruptedException;

    public boolean waitForMessage(String msg, long timeout) throws InterruptedException;

    public String getMessage();

    public List<String> getMessages();

    public List<String> getMessages(int to);

    public List<String> getMessages(int from, int to);

    public boolean waitForFinish(long timeout) throws InterruptedException;

    public void reset();
}
