



import java.beans.PropertyChangeEvent;

public final class Test7087429 {
    public static void main(String[] args) {
        try {
            new PropertyChangeEvent(null, null, null, null);
        }
        catch (IllegalArgumentException exception) {
            if (exception.getMessage().equals("null source")) {
                return;
            }
        }
        throw new Error("IllegalArgumentException expected");
    }
}
