import javax.management.Notification;

public class SqeNotification extends Notification {

    public SqeNotification(String type, Object source, long sequenceNumber) {
        super(type, source, sequenceNumber);
    }

    public SqeNotification(String type, Object source, long sequenceNumber, long timeStamp) {
        super(type, source, sequenceNumber, timeStamp);
    }

    public SqeNotification(String type, Object source, long sequenceNumber, long timeStamp, String message) {
        super(type, source, sequenceNumber, timeStamp, message);
    }

    public SqeNotification(String type, Object source, long sequenceNumber, String message) {
        super(type, source, sequenceNumber, message);
    }
}
